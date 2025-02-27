package com.solo.ecommerce.controller;

import com.solo.ecommerce.dto.request.LoginRequest;
import com.solo.ecommerce.dto.request.UserRequest;
import com.solo.ecommerce.dto.response.LoginResponse;
import com.solo.ecommerce.dto.response.UserResponse;
import com.solo.ecommerce.model.Role;
import com.solo.ecommerce.service.AuthService;
import com.solo.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body("Berhasil Hapus User");
    }

    @PatchMapping("/role/{username}")
    public ResponseEntity<?> updateRole(@PathVariable String username,@RequestParam Role set) {
        userService.updateUserRole(username, set);
        return ResponseEntity.status(HttpStatus.OK).body("Berhasil update user role.");
    }

    @GetMapping("/all")
    public ResponseEntity<?> allUser(@RequestParam(required = false) Role role, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<UserResponse> users = userService.findAllUser(role, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }




}
