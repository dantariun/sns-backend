package com.showme.admin.member.controller;

import com.showme.admin.member.dto.*;
import com.showme.admin.member.service.AdminMemberService;
import com.showme.core.common.ApiResponse;
import com.showme.core.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "회원 관리")
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService memberService;

    @Operation(summary = "회원 목록", description = "닉네임·이메일 검색, 상태·가입일 필터, 페이지네이션")
    @GetMapping
    public ApiResponse<PageResponse<MemberSummaryResponse>> search(
            @ParameterObject MemberSearchCondition condition,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(memberService.search(condition, pageable));
    }

    @Operation(summary = "회원 상세")
    @GetMapping("/{id}")
    public ApiResponse<MemberDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(memberService.getDetail(id));
    }

    @Operation(summary = "회원 피드", description = "삭제·블라인드된 게시물도 포함")
    @GetMapping("/{id}/posts")
    public ApiResponse<PageResponse<MemberPostResponse>> posts(
            @PathVariable Long id,
            @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.ok(memberService.getPosts(id, pageable));
    }

    @Operation(summary = "팔로워 목록")
    @GetMapping("/{id}/followers")
    public ApiResponse<PageResponse<MemberRelationResponse>> followers(
            @PathVariable Long id, @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(memberService.getFollowers(id, pageable));
    }

    @Operation(summary = "팔로잉 목록")
    @GetMapping("/{id}/followings")
    public ApiResponse<PageResponse<MemberRelationResponse>> followings(
            @PathVariable Long id, @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(memberService.getFollowings(id, pageable));
    }

    @Operation(summary = "제재 이력")
    @GetMapping("/{id}/sanction-history")
    public ApiResponse<List<SanctionHistoryResponse>> sanctionHistory(@PathVariable Long id) {
        return ApiResponse.ok(memberService.getSanctionHistory(id));
    }

    @Operation(summary = "계정 정지", description = "days 를 생략하면 무기한 정지")
    @PatchMapping("/{id}/sanction")
    public ApiResponse<Void> suspend(@PathVariable Long id,
                                     @Valid @RequestBody SuspendRequest request,
                                     @AuthenticationPrincipal Long adminId) {
        memberService.suspend(id, request, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "정지 해제")
    @PatchMapping("/{id}/sanction/release")
    public ApiResponse<Void> release(@PathVariable Long id,
                                     @Valid @RequestBody(required = false) SanctionReasonRequest request,
                                     @AuthenticationPrincipal Long adminId) {
        memberService.releaseSuspension(id, reasonOf(request), adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "강제 탈퇴")
    @PatchMapping("/{id}/withdraw")
    public ApiResponse<Void> withdraw(@PathVariable Long id,
                                      @Valid @RequestBody(required = false) SanctionReasonRequest request,
                                      @AuthenticationPrincipal Long adminId) {
        memberService.forceWithdraw(id, reasonOf(request), adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "계정 복구")
    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id,
                                     @Valid @RequestBody(required = false) SanctionReasonRequest request,
                                     @AuthenticationPrincipal Long adminId) {
        memberService.restore(id, reasonOf(request), adminId);
        return ApiResponse.ok();
    }

    private static String reasonOf(SanctionReasonRequest request) {
        return request != null ? request.reason() : null;
    }
}
