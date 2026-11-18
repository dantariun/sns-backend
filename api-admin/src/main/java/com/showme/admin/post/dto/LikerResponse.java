package com.showme.admin.post.dto;

import java.time.LocalDateTime;

public record LikerResponse(Long memberId, String nickname, String profileImageUrl, LocalDateTime likedAt) {
}
