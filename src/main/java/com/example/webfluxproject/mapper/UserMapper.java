package com.example.webfluxproject.mapper;

import com.example.webfluxproject.dto.rest.UserDto;
import com.example.webfluxproject.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map(UserEntity userEntity);

    UserEntity map(UserDto userDto);

}
