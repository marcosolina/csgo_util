package com.ixigo.serverhelper.services.implementations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriBuilder;

import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.library.utils.DateUtils;
import com.ixigo.serverhelper.config.properties.DemFilesProperties;
import com.ixigo.serverhelper.config.properties.DemManagersEndPoints;
import com.ixigo.serverhelper.config.properties.MapsProperties;
import com.ixigo.serverhelper.constants.ErrorCodes;
import com.ixigo.serverhelper.models.svc.SvcServerMap;
import com.ixigo.serverhelper.services.interfaces.DemFilesService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

public class DemFilesServiceImp implements DemFilesService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFilesServiceImp.class);
	private static Map<String, Boolean> filesAlreadySent = new HashMap<String, Boolean>();

	@Autowired
	private DemFilesProperties demProps;
	@Autowired
	private DemManagersEndPoints demManagerEndPoints;
	@Autowired
	private MapsProperties mapsProps;
	@Autowired
	private IxigoWebClientUtils webClient;
	@Autowired
	private IxigoMessageResource msgSource;
	

	@Override
	public Mono<Boolean> postLastDemFiles(boolean isShutDown) throws IxigoException {
		// @formatter:off
		var flux = getListOfFiles()
		.filter(path -> path.getFileName().toString().endsWith(".dem"))
		.filter(path -> {
			LocalDate fileDate = DateUtils.fromStringToLocalDate(path.getFileName().toString().split("-")[1], DateFormats.FILE_NAME_JUST_DATE);
			return !demProps.getUploadFilesOnlyIfMonday() || fileDate.getDayOfWeek() == DayOfWeek.MONDAY;
		})
		.filter(path ->{
			Boolean alreadySent = filesAlreadySent.getOrDefault(path.getFileName().toString(), false);
			return !alreadySent;
		})
		// Sort descending
		.sort((o1, o2) -> {
			LocalDateTime ldt = DateUtils.fromDemFileNameToLocalDateTime(o1.toFile());
            LocalDateTime ldt2 = DateUtils.fromDemFileNameToLocalDateTime(o2.toFile());
            return ldt.compareTo(ldt2) * -1;
		});
		
		/*
         * The top one, which is the latest one, is the one which is locked
         * by the server. I cannot send it
         */
		if(!isShutDown) {
			flux = flux.skip(1);
		}
		
		// Skip small files (when no one is playing the map)
		return flux.filter(path -> getFileSizeInMegabyte(path) > 2)
		.flatMap(path -> postDemFile(path).map(resp -> Tuples.of(path, resp)))
		.map(tuple -> {
			Path demFile = tuple.getT1();
			ResponseEntity<Void> resp = tuple.getT2();
			boolean fileSent = resp.getStatusCode() == HttpStatus.CREATED; 
			return Tuples.of(demFile, fileSent);
		})
		.map(tuple ->{
			Path demFile = tuple.getT1();
			boolean fileSent = tuple.getT2();
			boolean fileDeleted = false;
			if(fileSent) {
				try {
					String fileName = demFile.getFileName().toString();
					_LOGGER.info(String.format("Uploaded dem file: %s", fileName));
					
					filesAlreadySent.put(fileName, true);
					if(demProps.getDeleteFileAfterUpload()) {						
						_LOGGER.info(String.format("Deleting dem file: %s", fileName));
						Files.delete(demFile);
						filesAlreadySent.remove(fileName);
					}
					fileDeleted = true;
				} catch (IOException e) {
					_LOGGER.error(e.getMessage());
				}
			}
			return Tuples.of(demFile, fileDeleted);
		})
		.collectList()
		.flatMap(list -> triggerParseNewDem())
		.map(resp -> {
			_LOGGER.info("Dem files sent");
			if(resp.getStatusCode().is2xxSuccessful()) {
				_LOGGER.info("Dem files process triggered succesfully");
			}else {
				_LOGGER.error("Not able to trigger the deb files process");
			}
			return resp.getStatusCode().is2xxSuccessful();
		})
		;
		// @formatter:on
	}
	
	@Override
	public Flux<SvcServerMap> getServerMaps() throws IxigoException {
		try {
			// @formatter:off
			String mapExtension = ".bsp";
			_LOGGER.debug(mapsProps.getRootFolder().toString());
			return Flux.fromStream(
					Files.walk(mapsProps.getRootFolder())
					.filter(Files::isRegularFile)
				)
				.filter(p -> p.toString().endsWith(mapExtension))
				.map(p -> {
					SvcServerMap map = new SvcServerMap();
					var fullPath = p.toAbsolutePath().toString();
					map.setWorkshop(fullPath.contains("workshop"));
					if(map.isWorkshop()) {
						int count = p.getNameCount();
						if (count > 2) {
						    String workshopId = p.getName(count - 2).toString();
						    map.setWorkshopId(workshopId);
						}
					}
					map.setMapName(p.getFileName().toString().replace(mapExtension, ""));
					return map;
				});
			// @formatter:on

		} catch (IOException e) {
			e.printStackTrace();
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.GENERIC_ERROR), ErrorCodes.GENERIC_ERROR);
		}
	}

	private Mono<ResponseEntity<Void>> triggerParseNewDem() {
		try {
			URL url = URI.create(demManagerEndPoints.getPostParseQueuedFiles()).toURL();
			_LOGGER.debug(String.format("Triggering the DEM parser at URL: %s", url.toString()));
			return webClient.performRequestNoExceptions(Void.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
		}
	}

	private Flux<Path> getListOfFiles() {
		try {
			var stream = Files.list(demProps.getDemFilesFolderFullPath());
			return Flux.fromStream(stream);
		} catch (IOException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "");
		}
	}

	private Mono<ResponseEntity<Void>> postDemFile(Path path) {
		try {
			// @formatter:off
			URL url = URI.create(demManagerEndPoints.getPostDemFile()).toURL();
			return getResource(path)
			.flatMap(resource -> {
				String fileName = path.getFileName().toString();
				MultipartBodyBuilder builder = new MultipartBodyBuilder();
				builder.part("file", resource).filename(fileName);
				
				MultiValueMap<String, HttpEntity<?>> parts = builder.build();
				
				var resp = webClient.getWebBuilder()
					.build()
					.post()
					.uri(uriBuilder -> {
	                    UriBuilder ub = uriBuilder
	                            .scheme(url.getProtocol())
	                            .host(url.getHost())
	                            .port(url.getPort())
	                            .path(url.getPath());
	                    return ub.build();
	                })
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.bodyValue(parts)
					.exchangeToMono(response -> Mono.just(new ResponseEntity<Void>(response.statusCode())))
					.onErrorResume(error -> Mono.error(new IxigoException(HttpStatus.BAD_GATEWAY, error.getMessage(), "")));
				return resp;
			})
			; 
			// @formatter:on
		} catch (MalformedURLException e) {
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "");
		}
	}

	private Mono<Resource> getResource(Path file) {
		return Mono.fromSupplier(() -> {
			try {
				Resource resource = new UrlResource(file.toUri());
				if (resource.exists() || resource.isReadable()) {
					return resource;
				}
				return null;
			} catch (MalformedURLException e) {
				_LOGGER.error(e.getMessage());
				throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "");
			}
		}).subscribeOn(Schedulers.boundedElastic());
	}

	private long getFileSizeInMegabyte(Path p) {
		try {
			long bytes = Files.size(p);
			long kb = bytes / 1024;
			long mb = kb / 1024;
			return mb;
		} catch (Exception e) {
			_LOGGER.error(e.getMessage());
			return 0;
		}
	}
}
