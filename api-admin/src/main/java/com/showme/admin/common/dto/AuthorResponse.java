package com.showme.admin.common.dto;

import com.showme.core.entity.member.Member;

public record AuthorResponse(Long id, String nickname, String profileImageUrl) {

    public static AuthorResponse from(Member m) {
        return new AuthorResponse(m.getId(), m.getNickname(), m.getProfileImageUrl());
    }

    /** 탈퇴 등으로 회원 정보를 찾지 못한 경우 */
    public static AuthorResponse unknown(Long id) {
        return new AuthorResponse(id, null, null);
    }
}
