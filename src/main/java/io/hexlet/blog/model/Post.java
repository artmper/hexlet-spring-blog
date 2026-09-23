package io.hexlet.blog.model;

import java.time.LocalDateTime;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Post {
    private String slug;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
