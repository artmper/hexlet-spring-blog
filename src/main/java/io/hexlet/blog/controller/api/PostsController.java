package io.hexlet.blog.controller.api;

import io.hexlet.blog.model.Post;

import io.hexlet.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    @Autowired private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "5") Integer limit) {
        var limitedPosts = postRepository.findAll()
                .stream()
                .limit(limit)
                .toList();

        return ResponseEntity.ok(limitedPosts);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        var post = postRepository.findById(id);

        return ResponseEntity.of(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody Post data, @PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        post.setContent(data.getContent());
        post.setTitle(data.getTitle());
        post.setPublished(data.isPublished());
        postRepository.save(post);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
