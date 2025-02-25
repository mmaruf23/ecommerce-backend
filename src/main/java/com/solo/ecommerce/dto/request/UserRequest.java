package com.solo.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "Username cannot ku ha ku!")
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank(message = "Email is required!")
    @Email
    private String email;

    @NotBlank(message = "Password cannot ku ha ku")
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank(message = "Kimi no na wa ?")
    @Size(min = 2, max = 20)
    private String name;
}
