package com.ixigo.demmanager.services.interfaces;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ixigo.demmanager.models.svc.SvcFileInfo;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

public interface DemFileManager {
	/**
	 * It saves the file and return the path of the saved file
	 * 
	 * @param file
	 * @return
	 * @throws MarcoException
	 */
	public Mono<Path> store(MultipartFile file) throws IxigoException;

	/**
	 * It returns the file
	 * 
	 * @param filename
	 * @return
	 */
	public Mono<Resource> load(String filename) throws IxigoException;

	/**
	 * It removes the file from the queue
	 * 
	 * @param filename
	 * @return
	 * @throws IxigoException
	 */
	public Mono<Boolean> removeFromQueue(String filename) throws IxigoException;

	/**
	 * It returns a list of available Paths
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public Mono<Map<String, List<SvcFileInfo>>> loadAllFileNames() throws IxigoException;
}
