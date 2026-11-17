package com.showme.core.entity.post;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 2000)
    private String content;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;

    @Column(nullable = false)
    private boolean blinded = false;

    private LocalDateTime deletedAt;

    @Builder
    public Post(Long memberId, String content, String thumbnailUrl) {
        this.memberId = memberId;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isDeleted() { return this.deletedAt != null; }
}
