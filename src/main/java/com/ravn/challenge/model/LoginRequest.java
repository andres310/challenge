package com.ravn.challenge.model;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

	@NotBlank(message = "Username is required.")
	String username;
	@NotBlank(message = "Password is required.")
	String password;
}
