package com.showme.admin.post.service;

import com.querydsl.core.Tuple;
import com.showme.admin.common.dto.AuthorResponse;
import com.showme.admin.post.dto.*;
import com.showme.admin.post.repository.AdminPostQueryRepository;
import com.showme.core.common.PageResponse;
import com.showme.core.entity.member.Member;
import com.showme.core.entity.post.Post;
import com.showme.core.entity.post.PostComment;
import com.showme.core.entity.report.ReportTargetType;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import com.showme.core.repository.member.MemberRepository;
import com.showme.core.repository.post.PostCommentRepository;
import com.showme.core.repository.post.PostRepository;
import com.showme.core.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.showme.core.entity.member.QMember.member;
import static com.showme.core.entity.post.QPost.post;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPostService {

    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final AdminPostQueryRepository queryRepository;

    public PageResponse<FeedResponse> search(FeedSearchCondition cond, Pageable pageable) {
        Page<Tuple> page = queryRepository.search(cond, pageable);
        Map<Long, Long> reportCounts = queryRepository.countReports(
                page.getContent().stream().map(t -> t.get(post).getId()).toList());

        return PageResponse.of(page.map(t -> {
            Post p = t.get(post);
            Member author = t.get(member);
            return FeedResponse.of(p, toAuthor(author, p.getMemberId()), reportCounts.getOrDefault(p.getId(), 0L));
        }));
    }

    public FeedResponse getDetail(Long postId) {
        Post p = findPost(postId);
        AuthorResponse author = memberRepository.findById(p.getMemberId())
                .map(AuthorResponse::from)
                .orElseGet(() -> AuthorResponse.unknown(p.getMemberId()));
        return FeedResponse.of(p, author, reportRepository.countByTargetTypeAndTargetId(ReportTargetType.FEED, postId));
    }

    public PageResponse<CommentResponse> getComments(Long postId, Pageable pageable) {
        findPost(postId);
        Page<PostComment> page = commentRepository.findByPostId(postId, pageable);
        Map<Long, Member> authors = memberRepository.findAllById(
                        page.getContent().stream().map(PostComment::getMemberId).distinct().toList())
                .stream().collect(Collectors.toMap(Member::getId, Function.identity()));

        return PageResponse.of(page.map(c ->
                CommentResponse.of(c, toAuthor(authors.get(c.getMemberId()), c.getMemberId()))));
    }

    public PageResponse<LikerResponse> getLikers(Long postId, Pageable pageable) {
        findPost(postId);
        return PageResponse.of(queryRepository.findLikers(postId, pageable));
    }

    @Transactional
    public void blind(Long postId) {
        Post p = findPost(postId);
        if (p.isDeleted()) throw new BusinessException(ErrorCode.POST_ALREADY_DELETED);
        if (p.isBlinded()) throw new BusinessException(ErrorCode.POST_ALREADY_BLINDED);
        p.blind();
    }

    @Transactional
    public void unblind(Long postId) {
        Post p = findPost(postId);
        if (p.isDeleted()) throw new BusinessException(ErrorCode.POST_ALREADY_DELETED);
        if (!p.isBlinded()) throw new BusinessException(ErrorCode.POST_NOT_BLINDED);
        p.unblind();
    }

    @Transactional
    public void delete(Long postId) {
        Post p = findPost(postId);
        if (p.isDeleted()) throw new BusinessException(ErrorCode.POST_ALREADY_DELETED);
        p.delete();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        PostComment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
        if (c.isDeleted()) throw new BusinessException(ErrorCode.COMMENT_ALREADY_DELETED);
        c.delete();
        postRepository.findById(c.getPostId()).ifPresent(Post::decreaseCommentCount);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    private static AuthorResponse toAuthor(Member author, Long memberId) {
        return author != null ? AuthorResponse.from(author) : AuthorResponse.unknown(memberId);
    }
}
