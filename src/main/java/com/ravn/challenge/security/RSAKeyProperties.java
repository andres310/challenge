package com.ravn.challenge.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * @author andres
 */
@Data
@ConfigurationProperties(prefix = "rsa")
public class RSAKeyProperties {
	RSAPublicKey publicKey;
	RSAPrivateKey privateKey;
}
