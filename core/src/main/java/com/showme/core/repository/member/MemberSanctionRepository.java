package com.showme.core.repository.member;

import com.showme.core.entity.member.MemberSanction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSanctionRepository extends JpaRepository<MemberSanction, Long> {

    List<MemberSanction> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
