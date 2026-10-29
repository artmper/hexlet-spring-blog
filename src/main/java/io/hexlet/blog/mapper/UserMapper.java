package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.user.CreateUserDTO;
import io.hexlet.blog.dto.user.UserDTO;
import io.hexlet.blog.dto.user.UserPatchDTO;
import io.hexlet.blog.dto.user.UserUpdateDTO;
import io.hexlet.blog.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {
    public abstract UserDTO toDTO(User user);

    public abstract User toEntity(CreateUserDTO dto);

    public abstract void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);

    public abstract void patchEntityFromDTO(UserPatchDTO dto, @MappingTarget User user);
}
