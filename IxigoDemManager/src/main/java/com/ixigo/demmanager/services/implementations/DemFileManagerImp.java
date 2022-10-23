package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.database.Dem_process_queueDto;
import com.ixigo.demmanager.models.svc.SvcFileInfo;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.utils.DateUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 
 * @author Marco
 *
 */
public class DemFileManagerImp implements DemFileManager {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileManagerImp.class);

	@Autowired
	private DemFileManagerProps props;
	@Autowired
	private IxigoMessageResource msgSource;
	@Autowired
	private RepoProcessQueue repo;

	@Override
	public Mono<Path> store(MultipartFile file) throws IxigoException {
		// @formatter:off
		return writeFileInFolder(file)
				.flatMap(path -> { // Save record into Database
					Dem_process_queueDto dto = new Dem_process_queueDto();
					dto.setFile_name(path.toFile().getAbsolutePath());
					dto.setProcess_status(DemProcessStatus.NOT_PROCESSED);
					dto.setQueued_on(DateUtils.getCurrentUtcDateTime());
					
					return repo.insertOrUpdate(dto).map(v -> path);
				});
		// @formatter:on
	}

	@Override
	public Mono<Resource> load(String filename) throws IxigoException {
		return Mono.fromSupplier(() -> {
			try {
				String folderName = getFolderFromFileName(filename);
				Path file = props.getRootFolder().resolve(folderName).resolve(filename);
				Resource resource = new UrlResource(file.toUri());

				if (resource.exists() || resource.isReadable()) {
					return resource;
				}
				return null;
			} catch (MalformedURLException e) {
				_LOGGER.error(e.getMessage());
				throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.ERROR_READING_DEM_FILE));
			}
		}).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<Map<String, List<SvcFileInfo>>> loadAllFileNames() throws IxigoException {
		return Mono.fromSupplier(() -> {
			Map<String, List<SvcFileInfo>> map = new TreeMap<>(Comparator.reverseOrder());

			List<File> files = new ArrayList<>();
			try (Stream<Path> walk = Files.walk(props.getRootFolder())) {
				walk.filter(p -> p.toFile().getName().endsWith(".dem")).map(Path::toFile).forEach(files::add);
			} catch (IOException e) {
				if (_LOGGER.isTraceEnabled()) {
					e.printStackTrace();
				}
				throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}

			files.stream().forEach(f -> {
				String folderName = getFolderFromFileName(f.getName());
				map.compute(folderName, (k, v) -> {
					if (v == null) {
						v = new ArrayList<>();
					}
					SvcFileInfo fi = new SvcFileInfo();
					fi.setName(f.getName());
					fi.setMapName(getMapFromFileName(f.getName()));

					// Get length of file in bytes
					long fileSizeInBytes = f.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					fi.setSize(new BigDecimal(fileSizeInMB).setScale(2, RoundingMode.DOWN).toString() + " MB");
					v.add(fi);

					return v;
				});
			});

			return map;
		}).subscribeOn(Schedulers.boundedElastic());
	}

	private String getFolderFromFileName(String fileName) {
		String[] arr = fileName.split("-");
		return DateUtils.fromLocalDateToString(DateUtils.fromStringToLocalDate(arr[1], DateFormats.FOLDER_NAME), DateFormats.DB_DATE);
	}

	private String getMapFromFileName(String fileName) {
		String[] arr = fileName.split("-");
		return arr[4];
	}

	private Mono<Path> writeFileInFolder(MultipartFile file) {
		// https://stackoverflow.com/questions/62903055/how-to-handle-file-access-in-a-reactive-environment
		return Mono.fromSupplier(() -> {
			try {
				String folderName = getFolderFromFileName(file.getOriginalFilename());
				Path fileFolder = props.getRootFolder().resolve(folderName);
				Files.createDirectories(fileFolder);
				Path fileDestination = fileFolder.resolve(file.getOriginalFilename());

				_LOGGER.debug(String.format("Saving file: %s", fileDestination.toString()));
				Files.copy(file.getInputStream(), fileDestination, StandardCopyOption.REPLACE_EXISTING);
				_LOGGER.debug("File saved");

				return fileDestination;
			} catch (Exception e) {
				_LOGGER.error(e.getMessage());
				throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.ERROR_WHILE_SAVING_DEM_FILE));
			}
		}).subscribeOn(Schedulers.boundedElastic());
	}

}
