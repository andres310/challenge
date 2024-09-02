package com.ravn.challenge.service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Definition of a storage service.
 * 
 * @author andres
 */
public interface IStorageService {
	
	/**
     * Initializes service.
     */
	void init();

    /**
     * Stores file.
     *
     * @param file.
     */
	void store(MultipartFile file);
	
	/**
	 * Stores file.
	 * 
	 * @param multipartFile
	 * @param name
	 */
	void store(MultipartFile multipartFile, String name);
	
	/**
	 * Stores file.
	 * 
	 * @param multipartFile File being saved.
	 * @param name New file name.
	 * @param storePath Path where file will be saved.
	 */
	void store(MultipartFile multipartFile, String name, String storePath);

    /**
     * Loads all files saved.
     *
     * @return Stream of Paths of saved files.
     */
	Stream<Path> loadAll();
	
	/**
	 * Loads all files names saved.
	 * 
	 * @return List of all names of files saved.
	 */
	List<String> listFiles();

    /**
     * Loads a file by it's name.
     *
     * @param filename.
     * @return Path of loaded file.
     */
	Path load(String filename);


    /**
     * Loads a file as a {@code Resource}.
     *
     * @param filename
     * @return Loaded file.
     */
	Resource loadAsResource(String filename);
	
    /**
     * Deletes all stored files.
     */
	void deleteAll();
}
