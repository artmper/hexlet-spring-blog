package io.hexlet.blog;

import io.hexlet.blog.model.Post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
public class Application {
    private List<Post> posts = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Hello from Spring Boot 4.1.0";
    }

    @GetMapping("/about")
    public String about() {
        return "This is simple Spring blog \uD83E\uDD17";
    }

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "5") Integer limit) {
        return posts.stream().limit(limit).toList();
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> show(@PathVariable String id) {
        var post = posts.stream().filter(p -> p.getSlug().equals(id)).findFirst();
        return post;
    }

    @PostMapping("/posts") // Создание страницы
    public Post create(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{id}")
    public Post update(@RequestBody Post data, @PathVariable String id) {
        var post = posts.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        post.setSlug(data.getSlug());
        post.setAuthor(data.getAuthor());
        post.setContent(data.getContent());
        post.setTitle(data.getTitle());
        post.setCreatedAt(data.getCreatedAt());

        return post;
    }

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getSlug().equals(id));
    }
}
