package com.bugtracker.api.Controller;

import com.bugtracker.api.Model.User;
import com.bugtracker.api.Payload.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.bugtracker.api.Service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAllUsers().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return ResponseEntity.ok(convertToResponse(user));
    }

    // Create new user (Admin only)
    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getRoleName());
        return ResponseEntity.status(201).body(new MessageResponse("User created successfully"));
    }

    // Update existing user (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.ok(new MessageResponse("User updated successfully"));
    }

    // Delete user by ID (Admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        // Do NOT include password or roleName here for security reasons
        response.setRoleName(user.getRole().getName());
        return response;
    }

}
