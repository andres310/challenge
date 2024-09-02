package com.ravn.challenge.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ravn.challenge.dto.RatingDTO;
import com.ravn.challenge.entity.Rating;

@Mapper(componentModel = "spring", uses = { UserDTOMapper.class, MovieDTOMapper.class })
public interface RatingDTOMapper {
	RatingDTO convert(Rating entity);
	Rating convert(RatingDTO dto);
	
}
