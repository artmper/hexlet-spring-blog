package io.hexlet.blog.controller.api;

import io.hexlet.blog.dto.user.CreateUserDTO;
import io.hexlet.blog.dto.user.UserDTO;
import io.hexlet.blog.dto.user.UserUpdateDTO;
import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.mapper.UserMapper;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;

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

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UsersController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> index(
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam(defaultValue = "1") Integer page) {
        var limitedUsers = userRepository.findAll()
                .stream()
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .map(userMapper::toDTO)
                .toList();

        return ResponseEntity.ok(limitedUsers);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserDTO userData) {
        var user = userMapper.toEntity(userData);
        userRepository.save(user);
        var response = userMapper.toDTO(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> show(@PathVariable Long id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User '" + id + "' not found"));
        var response = userMapper.toDTO(user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserUpdateDTO data, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '" + id + "' not found"));

        userMapper.updateEntityFromDTO(data, user);
        userRepository.save(user);
        var response = userMapper.toDTO(user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User '" + id + "' not found"));

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
