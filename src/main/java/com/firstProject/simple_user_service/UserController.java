package com.firstProject.simple_user_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(users.values());
    }

    // POST /users - create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User request) {
        Long id = idGenerator.getAndIncrement();
        User user = new User(id, request.getName(), request.getPhoneNumber());
        users.put(id, user);
        return ResponseEntity.ok(user);
    }

    // GET /users/{id} - get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = users.get(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // PATCH /users/{id} - update name or phone
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User request) {
        User existing = users.get(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            existing.setPhoneNumber(request.getPhoneNumber());
        }

        return ResponseEntity.ok(existing);
    }

    // DELETE /users/{id} - delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User removed = users.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
