package io.hexlet.blog.controller.api;

import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;

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
@RequestMapping("/api/users")
public class UsersController {
    @Autowired private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> index(
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam(defaultValue = "1") Integer page) {
        var limitedUsers = userRepository.findAll()
                .stream()
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .toList();

        return ResponseEntity.ok(limitedUsers);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        var user = userRepository.findById(id);

        return ResponseEntity.of(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User data, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setEmail(data.getEmail());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setBirthday(data.getBirthday());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
