package com.ravn.challenge.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ravn.challenge.exception.StorageException;
import com.ravn.challenge.exception.StorageFileNotFoundException;

/**
 * 
 * @author andres
 *
 */
@Service
public class SystemsStorageService implements IStorageService {

	@Value("${storage.location}")
	private String location;
	private Path rootLocation;

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("File is empty.");
			}
			Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize()
					.toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// Makes sure is the right destination
				throw new StorageException("Cannot save file outside of current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Error al guardar archvio.", e);
		}
	}
	
	@Override
	public void store(MultipartFile multipartFile, String name, String storePath) {
		try {
			if (multipartFile.isEmpty()) {
				throw new StorageException("File is empty.");
			}
			// Changes filename
			String filename = name + "." + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
			
			// Lookup paht and set with format: path + filename
			Path destinationFile = Path.of(storePath).resolve(filename).normalize()
					.toAbsolutePath();
			
			// Writes file in destination
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("An error ocurred while storing the file.", e);
		}
	}
	
	@Override
	public void store(MultipartFile multipartFile, String name) {
		try {
			if (multipartFile.isEmpty()) {
				throw new StorageException("File is empty.");
			}
			// Changes filename
			String filename = name + "." + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
			
			// Lookup paht and set with format: path + filename
			Path destinationFile = Path.of(this.location).resolve(filename).normalize()
					.toAbsolutePath();
			
			// Writes file in destination
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("An error ocurred while storing the file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Cannot read stored files.", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Cannot read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Cannot read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			// Creates path, if path already exists does nothing
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Cannot initialize store path.", e);
		}
	}
	
	@Override
	public List<String> listFiles() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(Path::toString)
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new StorageException("An error ocurred when reading stored files.", e);
		}
	}
	
	public Path getPath() {
		return this.rootLocation;
	}
	
	public void setPath(String path) {
		this.rootLocation = Paths.get(path);
	}

	@PostConstruct
	private void setRootLocation() {
		this.setPath(location);
	}
}
