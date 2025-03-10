package com.solo.ecommerce.service;

import com.solo.ecommerce.dto.request.LoginRequest;
import com.solo.ecommerce.dto.request.UserRequest;
import com.solo.ecommerce.dto.response.LoginResponse;
import com.solo.ecommerce.dto.response.UserResponse;
import com.solo.ecommerce.exception.AuthenticationException;
import com.solo.ecommerce.exception.DuplicateDataException;
import com.solo.ecommerce.model.Role;
import com.solo.ecommerce.model.User;
import com.solo.ecommerce.repository.UserRepository;
import com.solo.ecommerce.security.CustomUserDetails;
import com.solo.ecommerce.util.ConvertToResponse;
import com.solo.ecommerce.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


    @Transactional
    public UserResponse registerUser(UserRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateDataException("username already exist");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateDataException("Email already exist!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return ConvertToResponse.userToResponse(savedUser);

    }

    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new LoginResponse(token);
    }


    public UserResponse updateUser(User user, UserRequest request) {
        if (request.getUsername() != null && !Objects.equals(user.getUsername(), request.getUsername())) {
            if(userRepository.existsByUsername(request.getUsername())){
                throw new DuplicateDataException("username already taken!");
            }
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null && !Objects.equals(user.getEmail(), request.getEmail())) {
            if(userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateDataException("Email already taken!");
            }
            user.setEmail(request.getEmail());

        }
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getName() != null) user.setName(request.getName());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return ConvertToResponse.userToResponse(savedUser);
    }
}
