package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.post.PostCreateDTO;
import io.hexlet.blog.dto.post.PostDTO;
import io.hexlet.blog.dto.post.PostUpdateDTO;
import io.hexlet.blog.model.Post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "user.id", target = "userId")
    PostDTO toDTO(Post post);

    @Mapping(target = "user", ignore = true)
    Post toEntity(PostCreateDTO dto);

    void updateEntityFromDTO(PostUpdateDTO dto, @MappingTarget Post post);
}
