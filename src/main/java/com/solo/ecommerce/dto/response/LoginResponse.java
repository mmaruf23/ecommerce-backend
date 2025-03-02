package com.solo.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * response for login activity
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
}
