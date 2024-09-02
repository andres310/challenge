package com.ravn.challenge.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RatingDTO {

	UserDTO user;
	MovieDTO movie;
	/*Long idUser;
	Long idMovie;*/
	BigDecimal stars;
}
