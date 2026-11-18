package com.showme.core.entity.post;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PostVisibility visibility = PostVisibility.PUBLIC;

    @BatchSize(size = 100)
    @ElementCollection
    @CollectionTable(name = "post_media", joinColumns = @JoinColumn(name = "post_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "url", nullable = false, length = 500)
    private List<String> mediaUrls = new ArrayList<>();

    @BatchSize(size = 100)
    @ElementCollection
    @CollectionTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag", nullable = false, length = 50)
    private Set<String> tags = new LinkedHashSet<>();

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;

    @Column(nullable = false)
    private boolean blinded = false;

    private LocalDateTime deletedAt;

    @Builder
    public Post(Long memberId, String content, PostVisibility visibility,
                List<String> mediaUrls, Set<String> tags) {
        this.memberId = memberId;
        this.content = content;
        this.visibility = visibility != null ? visibility : PostVisibility.PUBLIC;
        if (mediaUrls != null) this.mediaUrls.addAll(mediaUrls);
        if (tags != null) this.tags.addAll(tags);
    }

    public boolean isDeleted() { return this.deletedAt != null; }

    public String getThumbnailUrl() {
        return mediaUrls.isEmpty() ? null : mediaUrls.get(0);
    }

    public void blind() { this.blinded = true; }
    public void unblind() { this.blinded = false; }
    public void delete() { this.deletedAt = LocalDateTime.now(); }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) this.commentCount--;
    }
}
