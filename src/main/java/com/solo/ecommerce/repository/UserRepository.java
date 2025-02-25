package com.solo.ecommerce.repository;

import com.solo.ecommerce.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(@NotBlank(message = "Username is required!") @Size(min = 4, max = 20) String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
