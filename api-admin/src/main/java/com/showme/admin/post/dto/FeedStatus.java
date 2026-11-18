package com.showme.admin.post.dto;

import com.showme.core.entity.post.Post;
import com.showme.core.entity.post.PostVisibility;

/** 관리자 화면용 상태 — 블라인드가 공개 범위보다 우선한다 */
public enum FeedStatus {
    PUBLIC,
    PRIVATE,
    BLINDED;

    public static FeedStatus of(Post post) {
        if (post.isBlinded()) return BLINDED;
        return post.getVisibility() == PostVisibility.PRIVATE ? PRIVATE : PUBLIC;
    }
}
