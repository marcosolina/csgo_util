package com.ixigo.demmanager.services.implementations;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ixigo.demmanager.models.svc.FileInfo;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

/**
 * 
 * @author Marco
 *
 */
public class DemFileManagerImp implements DemFileManager {

	@Override
	public Mono<Path> store(MultipartFile file) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Resource> load(String filename) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, List<FileInfo>>> loadAllFileNames() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

}
