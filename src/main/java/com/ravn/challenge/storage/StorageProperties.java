package com.ravn.challenge.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Configuration for {@code StorageService}.
 * 
 * @author andres
 */
@Data
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

	private String location;
}
