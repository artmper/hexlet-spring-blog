package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.user.CreateUserDTO;
import io.hexlet.blog.dto.user.UserDTO;
import io.hexlet.blog.dto.user.UserUpdateDTO;
import io.hexlet.blog.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(CreateUserDTO dto);

    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);
}
