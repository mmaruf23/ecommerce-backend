package com.solo.ecommerce.service;

import com.solo.ecommerce.dto.response.UserResponse;
import com.solo.ecommerce.exception.DataNotFoundException;
import com.solo.ecommerce.model.Role;
import com.solo.ecommerce.model.User;
import com.solo.ecommerce.repository.UserRepository;
import com.solo.ecommerce.util.ConvertToResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void deleteUserByUsername(String username) {
        if (!userRepository.existsByUsername(username)){
            throw new DataNotFoundException("Username tidak ditemukan!");
        }
        userRepository.deleteByUsername(username);
    }

    @Transactional
    public void updateUserRole(String username, Role role) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Usernama"+ username +" tidak ditemukan "));
        user.setRole(role);
        userRepository.save(user);
    }

    public Page<UserResponse> findAllUser(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        if (role == null){
            users = userRepository.findAll(pageable);
            return users.map(ConvertToResponse::userToResponse);
        }
        users = userRepository.findByRole(role, pageable);
        return users.map(ConvertToResponse::userToResponse);
    }



}
