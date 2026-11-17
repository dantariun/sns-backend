package com.showme.core.entity.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_sanction")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SanctionType type;

    @Column(length = 500)
    private String reason;

    private LocalDateTime suspendedUntil;

    @Column(nullable = false)
    private Long adminId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public MemberSanction(Long memberId, SanctionType type, String reason,
                          LocalDateTime suspendedUntil, Long adminId) {
        this.memberId = memberId;
        this.type = type;
        this.reason = reason;
        this.suspendedUntil = suspendedUntil;
        this.adminId = adminId;
    }
}
