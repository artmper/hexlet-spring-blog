package io.hexlet.blog.controller.api;

import io.hexlet.blog.mapper.PostMapper;
import io.hexlet.blog.dto.post.PostCreateDTO;
import io.hexlet.blog.dto.post.PostDTO;
import io.hexlet.blog.dto.post.PostUpdateDTO;
import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.repository.PostRepository;

import io.hexlet.blog.repository.UserRepository;
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
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public PostsController(PostRepository postRepository,
                           UserRepository userRepository,
                           PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by("createdAt").descending());
        var pagedPosts = postRepository.findByPublishedTrue(pageable);
        var postsDTO = pagedPosts.map(postMapper::toDTO);

        return ResponseEntity.ok(postsDTO);
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User with id=" + dto.getUserId() + " not found"));
        var post = postMapper.toEntity(dto);
        post.setUser(user);
        postRepository.save(post);

        var response = postMapper.toDTO(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        var post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post '" + id + "' not found"));

        var postDTO = postMapper.toDTO(post);
        return ResponseEntity.status(HttpStatus.OK).body(postDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@Valid @RequestBody PostUpdateDTO data, @PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post '" + id + "' not found"));

        postMapper.updateEntityFromDTO(data, post);
        postRepository.save(post);

        var response = postMapper.toDTO(post);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
