package com.showme.admin.post.dto;

import com.showme.admin.common.dto.AuthorResponse;
import com.showme.core.entity.post.Post;

import java.time.LocalDateTime;
import java.util.List;

public record FeedResponse(
        Long id,
        AuthorResponse author,
        String content,
        List<String> mediaUrls,
        int likeCount,
        int commentCount,
        long reportCount,
        FeedStatus status,
        List<String> tags,
        boolean deleted,
        LocalDateTime createdAt
) {
    public static FeedResponse of(Post post, AuthorResponse author, long reportCount) {
        return new FeedResponse(post.getId(), author, post.getContent(),
                List.copyOf(post.getMediaUrls()), post.getLikeCount(), post.getCommentCount(), reportCount,
                FeedStatus.of(post), List.copyOf(post.getTags()), post.isDeleted(), post.getCreatedAt());
    }
}
