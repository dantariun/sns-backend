package com.showme.admin.notice.controller;

import com.showme.admin.notice.dto.NoticeRequest;
import com.showme.admin.notice.dto.NoticeResponse;
import com.showme.admin.notice.dto.NoticeSearchCondition;
import com.showme.admin.notice.service.AdminNoticeService;
import com.showme.core.common.ApiResponse;
import com.showme.core.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService noticeService;

    @Operation(summary = "공지사항 목록", description = "제목 검색, 게시 여부 필터")
    @GetMapping
    public ApiResponse<PageResponse<NoticeResponse>> search(
            @ParameterObject NoticeSearchCondition condition,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(noticeService.search(condition, pageable));
    }

    @Operation(summary = "공지사항 상세")
    @GetMapping("/{id}")
    public ApiResponse<NoticeResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(noticeService.getDetail(id));
    }

    @Operation(summary = "공지사항 작성", description = "생성된 공지 id 반환")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> create(@Valid @RequestBody NoticeRequest request,
                                    @AuthenticationPrincipal Long adminId) {
        return ApiResponse.ok(noticeService.create(request, adminId));
    }

    @Operation(summary = "공지사항 수정")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody NoticeRequest request) {
        noticeService.update(id, request);
        return ApiResponse.ok();
    }

    @Operation(summary = "공지사항 삭제", description = "소프트 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        noticeService.delete(id);
        return ApiResponse.ok();
    }
}
