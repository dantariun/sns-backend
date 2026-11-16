package com.showme.admin.auth.controller;

import com.showme.admin.auth.dto.AdminLoginRequest;
import com.showme.admin.auth.dto.AdminRefreshRequest;
import com.showme.admin.auth.dto.AdminTokenResponse;
import com.showme.admin.auth.service.AdminAuthService;
import com.showme.core.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 인증")
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(summary = "로그인", description = "아이디·비밀번호로 Access/Refresh 토큰을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<AdminTokenResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.ok(adminAuthService.login(request));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh 토큰으로 새 토큰 쌍을 발급합니다. 사용한 Refresh 토큰은 폐기됩니다.")
    @PostMapping("/refresh")
    public ApiResponse<AdminTokenResponse> refresh(@Valid @RequestBody AdminRefreshRequest request) {
        return ApiResponse.ok(adminAuthService.refresh(request));
    }

    @Operation(summary = "로그아웃", description = "해당 관리자의 모든 Refresh 토큰을 폐기합니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal Long adminId) {
        adminAuthService.logout(adminId);
        return ApiResponse.ok();
    }
}
