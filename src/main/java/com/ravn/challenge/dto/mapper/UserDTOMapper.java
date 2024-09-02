package com.ravn.challenge.dto.mapper;

import org.mapstruct.Mapper;

import com.ravn.challenge.dto.UserDTO;
import com.ravn.challenge.entity.User;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

	UserDTO convert(User entity);
	User convert(UserDTO dto);
}
