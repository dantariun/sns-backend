package com.showme.admin.post.dto;

import com.showme.admin.common.dto.AuthorResponse;
import com.showme.core.entity.post.PostComment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long parentId,
        AuthorResponse author,
        String content,
        int likeCount,
        boolean blinded,
        boolean deleted,
        LocalDateTime createdAt
) {
    public static CommentResponse of(PostComment c, AuthorResponse author) {
        return new CommentResponse(c.getId(), c.getParentId(), author, c.getContent(),
                c.getLikeCount(), c.isBlinded(), c.isDeleted(), c.getCreatedAt());
    }
}
