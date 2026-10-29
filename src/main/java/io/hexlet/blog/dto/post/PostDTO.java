package io.hexlet.blog.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private Long userId;
}
