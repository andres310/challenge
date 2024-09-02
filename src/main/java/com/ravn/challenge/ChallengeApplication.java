package com.ravn.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.ravn.challenge.security.RSAKeyProperties;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(RSAKeyProperties.class)
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

}
