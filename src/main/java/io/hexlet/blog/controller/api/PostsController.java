package io.hexlet.blog.controller.api;

import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostRepository postRepository;

    public PostsController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by("createdAt").descending());
        var pagedPosts = postRepository.findByPublishedTrue(pageable);

        return ResponseEntity.ok(pagedPosts);
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
        Post savedPost = postRepository.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        var post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post '" + id + "' not found"));

        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@Valid @RequestBody Post data, @PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post '" + id + "' not found"));

        post.setContent(data.getContent());
        post.setTitle(data.getTitle());
        post.setPublished(data.isPublished());
        var savedPost = postRepository.save(post);

        return ResponseEntity.ok(savedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
