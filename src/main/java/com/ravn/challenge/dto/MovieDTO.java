package com.ravn.challenge.dto;

import javax.validation.constraints.NotBlank;

import com.ravn.challenge.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

	Long idMovie;
	@NotBlank(message = "Name cannot be blank.")
	String name;
	@NotBlank(message = "Synopsis cannot be blank.")
	String synopsis;
	String imageUrl;
	@NotBlank(message = "Release year cannot be blank.")
	String year;
	Category category;
}
