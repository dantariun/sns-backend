package com.showme.admin.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** days 가 null 이면 무기한 정지 */
public record SuspendRequest(
        @NotBlank(message = "정지 사유를 입력해주세요.") @Size(max = 500) String reason,
        @Positive(message = "정지 기간은 1일 이상이어야 합니다.") Integer days
) {
}
