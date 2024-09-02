package com.ravn.challenge.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ravn.challenge.combinator.ImageExtensionValidator;
import com.ravn.challenge.dto.MovieDTO;
import com.ravn.challenge.dto.mapper.MovieDTOMapper;
import com.ravn.challenge.entity.Category;
import com.ravn.challenge.entity.Movie;
import com.ravn.challenge.service.CategoryService;
import com.ravn.challenge.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/api/v1/movies")
public class MovieController {
	
	private final MovieService movieService;
	private final MovieDTOMapper movieDTOMapper;
	private final CategoryService categoryService;
	
	public MovieController(MovieService movieService, MovieDTOMapper movieDTOMapper,
			CategoryService categoryService) {
		this.movieService = movieService;
		this.movieDTOMapper = movieDTOMapper;
		this.categoryService = categoryService;
	}
	
	@Operation(summary = "Get all movies", description = "Retrieve a list of all movies.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of movies",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = MovieDTO.class)))
    })
	@GetMapping
	public List<MovieDTO> getAllMovies() {
		return this.movieService.getAll().stream()
				.map(this.movieDTOMapper::convert)
				.collect(Collectors.toList());
	}
	
	@Operation(summary = "Get paginated and filtered movies", 
            description = "Retrieve a paginated and filtered list of movies with optional search, filter, and sort criteria.")
	@Parameters({
		@Parameter(in = ParameterIn.QUERY, name = "searchTerm", description = "Search term for movie name or synopsis"),
		@Parameter(in = ParameterIn.QUERY, name = "categoryId", description = "Filter by category ID"),
		@Parameter(in = ParameterIn.QUERY, name = "yearOfRelease", description = "Filter by year of release"),
		@Parameter(in = ParameterIn.QUERY, name = "page", description = "Page number for pagination", example = "0"),
		@Parameter(in = ParameterIn.QUERY, name = "size", description = "Number of records per page", example = "10"),
		@Parameter(in = ParameterIn.QUERY, name = "sortBy", description = "Field to sort by", example = "idMovie"),
		@Parameter(in = ParameterIn.QUERY, name = "order", description = "Sort order: asc or desc", example = "asc")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of movies",
					content = @Content(mediaType = "application/json",
						schema = @Schema(implementation = Movie.class)))
	})
	@GetMapping("/page")
	public Page<Movie> getMovies(
	        @RequestParam(required = false) String searchTerm,
	        @RequestParam(required = false) Long categoryId,
	        @RequestParam(required = false) String yearOfRelease,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "idMovie") String sortBy,
	        @RequestParam(defaultValue = "asc") String order
	    ) {
		Category category = null;
		if (categoryId != null) {
			category = this.categoryService.findById(categoryId);
		}

		Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

	    return movieService.getMovies(searchTerm, category, yearOfRelease, pageable);
	}
	
	@Operation(summary = "Get movie image", description = "Retrieve the image of a movie by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved movie image",
                     content = @Content(mediaType = "image/jpeg")),
        @ApiResponse(responseCode = "404", description = "Movie image not found")
    })
	@GetMapping(value = "/image/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
	public ResponseEntity<Resource> getImage(@PathVariable Long id) {
		var file = this.movieService.loadImage(id);
    	if (file == null)
    		return ResponseEntity.notFound().build();
    		
    	return ResponseEntity.ok()
    			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
    			.body(file);
	}
	
	@Operation(summary = "Create a new movie", description = "Create a new movie record.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created movie",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = MovieDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
	@PostMapping
	public MovieDTO createMovie(@RequestBody @Valid MovieDTO dto) {
		Movie entity = this.movieDTOMapper.convert(dto);
		return this.movieDTOMapper.convert(this.movieService.save(entity));
	}
	
	@Operation(summary = "Upload an image for a movie", description = "Upload an image file associated with a movie by its ID.")
    @Parameters({
        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the movie", required = true),
        @Parameter(in = ParameterIn.QUERY, name = "img", description = "Image file to upload", required = true, 
                   content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully uploaded image"),
        @ApiResponse(responseCode = "400", description = "Invalid file format")
    })
	@PostMapping("/image/{id}")
	public String uploadImage(@RequestParam MultipartFile img, @PathVariable Long id) throws Exception {
		String extension = img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf(".") + 1);
    	
    	Boolean result = ImageExtensionValidator
    		.isJpeg()
    		.or(ImageExtensionValidator.isJpg())
    		.or(ImageExtensionValidator.isPng())
    		.apply(extension);
    	
    	// TODO: Refactor
    	if (!result)
    		throw new Exception(String.format("Extension .%s not Valid ones are: .jpeg, .jpg o .png", extension));
    	
    	this.movieService.saveImage(img, id);
    	return "Image saved";
	}
	
	@Operation(summary = "Update a movie", description = "Update an existing movie by its ID.")
    @Parameters({
        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the movie", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated movie",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = MovieDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
	@PutMapping("/{id}")
	public MovieDTO updateMovie(@RequestBody @Valid MovieDTO dto, @PathVariable Long id) {
		Movie entity = this.movieDTOMapper.convert(dto);
		return this.movieDTOMapper.convert(this.movieService.save(entity));
	}
	
	@Operation(summary = "Delete a movie", description = "Delete a movie by its ID.")
    @Parameters({
        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the movie", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted movie"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
	@DeleteMapping("/{id}")
	public ResponseEntity deleteMovie(@PathVariable Long id) {
		this.movieService.delete(id);
		return ResponseEntity.ok("Movie deleted");
	}
}
