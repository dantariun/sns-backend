package com.showme.core.entity.notice;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private Long adminId;

    private LocalDateTime deletedAt;

    @Builder
    public Notice(String title, String content, boolean published, Long adminId) {
        this.title = title;
        this.content = content;
        this.published = published;
        this.adminId = adminId;
    }

    public boolean isDeleted() { return this.deletedAt != null; }

    public void update(String title, String content, boolean published) {
        this.title = title;
        this.content = content;
        this.published = published;
    }

    public void delete() { this.deletedAt = LocalDateTime.now(); }
}
