package com.ravn.challenge.dto.mapper;

import org.mapstruct.Mapper;

import com.ravn.challenge.dto.MovieDTO;
import com.ravn.challenge.entity.Movie;

@Mapper(componentModel = "spring", uses = { CategoryDTOMapper.class })
public interface MovieDTOMapper {
	
	MovieDTO convert(Movie entity);
	Movie convert(MovieDTO dto);
}
