package com.ravn.challenge.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ravn.challenge.dto.RatingDTO;
import com.ravn.challenge.dto.creation.RatingCreationDTO;
import com.ravn.challenge.dto.mapper.RatingDTOMapper;
import com.ravn.challenge.entity.Movie;
import com.ravn.challenge.entity.Rating;
import com.ravn.challenge.entity.User;
import com.ravn.challenge.service.RatingService;
import com.ravn.challenge.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Validated
@RequestMapping("/api/v1/ratings")
public class RatingController {
    
	private final RatingService ratingService;
	private final UserService userService;
	private final RatingDTOMapper ratingDTOMapper;
	
	public RatingController(RatingService ratingService, UserService userService, RatingDTOMapper ratingDTOMapper) {
		this.ratingService = ratingService;
		this.userService = userService;
		this.ratingDTOMapper = ratingDTOMapper;
	}


	@Operation(summary = "Rate a movie", description = "Allows an authenticated user to rate a movie.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully rated the movie",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = RatingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "User or movie not found")
    })
	@PostMapping
	public RatingDTO rateMovie(@Valid @RequestBody RatingCreationDTO dto, Principal principal) {
		String email = principal.getName();
		User user = this.userService.findByEmail(email);
		
		Rating rating = new Rating(new Movie(dto.getIdMovie()), user, dto.getStars());
		
		return ratingDTOMapper.convert(this.ratingService.save(rating));
	}
}
