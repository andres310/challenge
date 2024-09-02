package com.ravn.challenge.dto.mapper;

import org.mapstruct.Mapper;

import com.ravn.challenge.dto.CategoryDTO;
import com.ravn.challenge.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryDTOMapper {

	CategoryDTO convert(Category entity);
	Category convert(CategoryDTO dto);
}
