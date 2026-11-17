package com.showme.admin.member.dto;

import jakarta.validation.constraints.Size;

public record SanctionReasonRequest(
        @Size(max = 500) String reason
) {
}
