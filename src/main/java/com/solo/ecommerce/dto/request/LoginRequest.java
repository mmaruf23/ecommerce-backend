package com.solo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username ti dak bi leh ko song!")
    private String username;
    @NotBlank(message = "Password ti dak bo leh ko song")
    private String password;
}
