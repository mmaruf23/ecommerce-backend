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

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
        return new CustomUserDetails(user);
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
        return convertToResponse(savedUser);

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



    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getCreatedAt()
        );
    }


}
