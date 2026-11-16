package com.showme.admin.auth.dto;

public record AdminTokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String role
) {
}
