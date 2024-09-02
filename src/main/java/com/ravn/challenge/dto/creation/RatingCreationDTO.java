package com.ravn.challenge.dto.creation;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RatingCreationDTO {

	@NotNull(message = "The id of the movie is required.")
	@Min(value = 1, message = "Not a valid movie id.")
	Long idMovie;
	@NotNull(message = "Num of Stars is required.")
	@DecimalMin(value = "0", message = "Minimum rating is 0.0 out of 5.0 stars.")
    @DecimalMax(value = "5", message = "Maximun rating is 5.0 out of 5.0 stars.")
	BigDecimal stars;
}
