package com.showme.admin.member.dto;

import com.showme.core.entity.post.Post;

import java.time.LocalDateTime;

public record MemberPostResponse(
        Long id,
        String content,
        String thumbnailUrl,
        int likeCount,
        int commentCount,
        boolean blinded,
        boolean deleted,
        LocalDateTime createdAt
) {
    public static MemberPostResponse from(Post p) {
        return new MemberPostResponse(p.getId(), p.getContent(), p.getThumbnailUrl(),
                p.getLikeCount(), p.getCommentCount(), p.isBlinded(), p.isDeleted(), p.getCreatedAt());
    }
}
