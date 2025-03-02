package com.solo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request for login
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot blank")
    private String username;
    @NotBlank(message = "Password cannot blank")
    private String password;
}
