package com.showme.core.entity.follow;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow",
        uniqueConstraints = @UniqueConstraint(name = "uk_follow_pair", columnNames = {"follower_id", "following_id"}))
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 팔로우를 건 회원 */
    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    /** 팔로우 대상 회원 */
    @Column(name = "following_id", nullable = false)
    private Long followingId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Follow(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
