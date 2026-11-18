package com.showme.admin.notice.service;

import com.showme.admin.notice.dto.NoticeRequest;
import com.showme.admin.notice.dto.NoticeResponse;
import com.showme.admin.notice.dto.NoticeSearchCondition;
import com.showme.admin.notice.repository.AdminNoticeQueryRepository;
import com.showme.core.common.PageResponse;
import com.showme.core.entity.notice.Notice;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import com.showme.core.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;
    private final AdminNoticeQueryRepository queryRepository;

    public PageResponse<NoticeResponse> search(NoticeSearchCondition cond, Pageable pageable) {
        return PageResponse.of(queryRepository.search(cond, pageable).map(NoticeResponse::from));
    }

    public NoticeResponse getDetail(Long id) {
        return NoticeResponse.from(findNotice(id));
    }

    @Transactional
    public Long create(NoticeRequest request, Long adminId) {
        return noticeRepository.save(Notice.builder()
                .title(request.title())
                .content(request.content())
                .published(request.isPublished())
                .adminId(adminId)
                .build()).getId();
    }

    @Transactional
    public void update(Long id, NoticeRequest request) {
        findNotice(id).update(request.title(), request.content(), request.isPublished());
    }

    @Transactional
    public void delete(Long id) {
        findNotice(id).delete();
    }

    private Notice findNotice(Long id) {
        return noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }
}
