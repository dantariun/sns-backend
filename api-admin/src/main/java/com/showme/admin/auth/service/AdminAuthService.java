package com.showme.admin.auth.service;

import com.showme.admin.auth.dto.AdminLoginRequest;
import com.showme.admin.auth.dto.AdminRefreshRequest;
import com.showme.admin.auth.dto.AdminTokenResponse;
import com.showme.admin.jwt.AdminJwtProvider;
import com.showme.core.entity.admin.AdminMember;
import com.showme.core.entity.admin.AdminRefreshToken;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import com.showme.core.repository.admin.AdminMemberRepository;
import com.showme.core.repository.admin.AdminRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminMemberRepository adminMemberRepository;
    private final AdminRefreshTokenRepository refreshTokenRepository;
    private final AdminJwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminTokenResponse login(AdminLoginRequest request) {
        // 아이디 존재 여부를 노출하지 않도록 같은 에러로 응답
        AdminMember admin = adminMemberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PASSWORD));

        if (!passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        if (!admin.isActive()) {
            throw new BusinessException(ErrorCode.ADMIN_INACTIVE);
        }

        admin.updateLastLoginAt();
        return issueTokens(admin);
    }

    @Transactional
    public AdminTokenResponse refresh(AdminRefreshRequest request) {
        AdminRefreshToken saved = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        // Refresh Token Rotation — 사용된 토큰은 즉시 폐기
        refreshTokenRepository.delete(saved);
        if (saved.isExpired()) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        AdminMember admin = adminMemberRepository.findById(saved.getAdminId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADMIN_NOT_FOUND));
        if (!admin.isActive()) {
            throw new BusinessException(ErrorCode.ADMIN_INACTIVE);
        }

        return issueTokens(admin);
    }

    @Transactional
    public void logout(Long adminId) {
        refreshTokenRepository.deleteByAdminId(adminId);
    }

    private AdminTokenResponse issueTokens(AdminMember admin) {
        String authority = admin.getRole().authority();
        String accessToken = jwtProvider.generateAccessToken(admin.getId(), authority);
        String refreshToken = jwtProvider.generateRefreshToken(admin.getId());

        refreshTokenRepository.save(AdminRefreshToken.builder()
                .adminId(admin.getId())
                .token(refreshToken)
                .expiresAt(jwtProvider.extractExpiration(refreshToken))
                .build());

        return new AdminTokenResponse(accessToken, refreshToken,
                jwtProvider.getAccessTokenExpiry() / 1000, admin.getRole().name());
    }
}
