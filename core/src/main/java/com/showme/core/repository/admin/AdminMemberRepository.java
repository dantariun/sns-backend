package com.showme.core.repository.admin;

import com.showme.core.entity.admin.AdminMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminMemberRepository extends JpaRepository<AdminMember, Long> {

    Optional<AdminMember> findByLoginId(String loginId);
}
