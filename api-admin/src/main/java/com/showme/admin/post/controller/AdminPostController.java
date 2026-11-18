package com.showme.admin.post.controller;

import com.showme.admin.post.dto.CommentResponse;
import com.showme.admin.post.dto.FeedResponse;
import com.showme.admin.post.dto.FeedSearchCondition;
import com.showme.admin.post.dto.LikerResponse;
import com.showme.admin.post.service.AdminPostService;
import com.showme.core.common.ApiResponse;
import com.showme.core.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "피드 관리")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService postService;

    @Operation(summary = "피드 목록", description = "본문·작성자 닉네임 검색, 상태·작성일 필터. 삭제된 게시물은 제외")
    @GetMapping("/posts")
    public ApiResponse<PageResponse<FeedResponse>> search(
            @ParameterObject FeedSearchCondition condition,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(postService.search(condition, pageable));
    }

    @Operation(summary = "피드 상세", description = "미디어·태그·신고 수 포함. 삭제된 게시물도 조회 가능")
    @GetMapping("/posts/{id}")
    public ApiResponse<FeedResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(postService.getDetail(id));
    }

    @Operation(summary = "피드 댓글 목록", description = "답글·삭제·블라인드 댓글 포함")
    @GetMapping("/posts/{id}/comments")
    public ApiResponse<PageResponse<CommentResponse>> comments(
            @PathVariable Long id,
            @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.ok(postService.getComments(id, pageable));
    }

    @Operation(summary = "피드 좋아요 목록")
    @GetMapping("/posts/{id}/likes")
    public ApiResponse<PageResponse<LikerResponse>> likes(
            @PathVariable Long id, @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(postService.getLikers(id, pageable));
    }

    @Operation(summary = "블라인드 처리")
    @PatchMapping("/posts/{id}/blind")
    public ApiResponse<Void> blind(@PathVariable Long id) {
        postService.blind(id);
        return ApiResponse.ok();
    }

    @Operation(summary = "블라인드 해제")
    @PatchMapping("/posts/{id}/unblind")
    public ApiResponse<Void> unblind(@PathVariable Long id) {
        postService.unblind(id);
        return ApiResponse.ok();
    }

    @Operation(summary = "피드 삭제", description = "소프트 삭제")
    @DeleteMapping("/posts/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ApiResponse.ok();
    }

    @Operation(summary = "댓글 삭제", description = "소프트 삭제, 게시물 댓글 수 차감")
    @DeleteMapping("/comments/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable Long id) {
        postService.deleteComment(id);
        return ApiResponse.ok();
    }
}
