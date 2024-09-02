package com.ravn.challenge.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ravn.challenge.dto.CategoryDTO;
import com.ravn.challenge.dto.mapper.CategoryDTOMapper;
import com.ravn.challenge.entity.Category;
import com.ravn.challenge.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Validated
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryDTOMapper categoryDTOMapper;

	public CategoryController(CategoryService categoryService, CategoryDTOMapper categoryDTOMapper) {
		this.categoryService = categoryService;
		this.categoryDTOMapper = categoryDTOMapper;
	}
	
	@Operation(summary = "Get all categories", description = "Retrieve a list of all categories.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = CategoryDTO.class)))
    })    
	@GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.findAll().stream()
                .map(categoryDTOMapper::convert)
                .collect(Collectors.toList());
    }

	@Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved category",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        return categoryDTOMapper.convert(categoryService.findById(id));
    }

	@Operation(summary = "Create a new category", description = "Create a new category based on the provided data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created category",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        Category category = categoryDTOMapper.convert(categoryDTO);
        return categoryDTOMapper.convert(categoryService.save(category));
    }

	@Operation(summary = "Update an existing category", description = "Update an existing category with the given ID and data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated category",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@RequestBody @Valid CategoryDTO categoryDTO, @PathVariable Long id) {
        Category category = categoryDTOMapper.convert(categoryDTO);
        category.setIdCategory(id);  // Ensure the ID is set on the entity
        return categoryDTOMapper.convert(categoryService.update(category));
    }

	@Operation(summary = "Delete a category", description = "Delete a category by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted category"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
