package com.showme.core.repository.admin;

import com.showme.core.entity.admin.AdminRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRefreshTokenRepository extends JpaRepository<AdminRefreshToken, Long> {

    Optional<AdminRefreshToken> findByToken(String token);

    void deleteByAdminId(Long adminId);
}
