package com.showme.core.entity.post;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long memberId;

    /** NULL 이면 최상위 댓글, 값이 있으면 답글 */
    private Long parentId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private boolean blinded = false;

    private LocalDateTime deletedAt;

    @Builder
    public PostComment(Long postId, Long memberId, Long parentId, String content) {
        this.postId = postId;
        this.memberId = memberId;
        this.parentId = parentId;
        this.content = content;
    }

    public boolean isDeleted() { return this.deletedAt != null; }

    public void blind() { this.blinded = true; }
    public void delete() { this.deletedAt = LocalDateTime.now(); }
}
