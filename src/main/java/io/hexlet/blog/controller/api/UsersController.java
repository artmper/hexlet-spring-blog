package io.hexlet.blog.controller.api;

import io.hexlet.blog.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final List<User> users = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<User>> index(
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam(defaultValue = "1") Integer page) {
        var limitedUsers = users.stream().skip((long) (page - 1) * limit).limit(limit).toList();

        return ResponseEntity.ok(limitedUsers);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User User) {
        users.add(User);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(User.getId())
                .toUri();

        return ResponseEntity.created(location).body(User);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        var User = users.stream().filter(u -> u.getId().equals(id)).findFirst();

        return ResponseEntity.of(User);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User data, @PathVariable Long id) {
        var User = users.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User.setId(data.getId());
        User.setName(data.getName());
        User.setEmail(data.getEmail());

        return ResponseEntity.ok(User);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        users.removeIf(p -> p.getId().equals(id));
        return ResponseEntity.noContent().build();
    }
}
