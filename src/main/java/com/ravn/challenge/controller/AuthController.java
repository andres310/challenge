package com.ravn.challenge.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ravn.challenge.model.LoginRequest;
import com.ravn.challenge.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 
 * @author andres
 */
@RestController
@Validated
@RequestMapping("/api/v1/p/auth")
public class AuthController {
	
	final TokenService tokenService;
	final AuthenticationManager authenticationManager;
	
	public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
		this.tokenService = tokenService;
		this.authenticationManager = authenticationManager;
	}

	@Operation(summary = "Ping the server", description = "A simple ping endpoint to check server availability.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server is available and responding with 'pong'")
    })
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	@Operation(summary = "Generate authentication token", description = "Generate a JWT token based on the provided username and password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated token",
                     content = @Content(mediaType = "text/plain",
                                        schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
	@PostMapping("/token")
	public String generateToken(@RequestBody LoginRequest userLogin) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
		return tokenService.generateToken(authentication);
	}
}
