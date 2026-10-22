package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.CreateUserDTO;
import io.hexlet.blog.dto.UserDTO;
import io.hexlet.blog.model.User;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(CreateUserDTO dto);
}
