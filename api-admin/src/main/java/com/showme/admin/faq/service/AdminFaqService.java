package com.showme.admin.faq.service;

import com.showme.admin.faq.dto.FaqRequest;
import com.showme.admin.faq.dto.FaqResponse;
import com.showme.admin.faq.dto.FaqSearchCondition;
import com.showme.admin.faq.repository.AdminFaqQueryRepository;
import com.showme.core.common.PageResponse;
import com.showme.core.entity.faq.Faq;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import com.showme.core.repository.faq.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFaqService {

    private final FaqRepository faqRepository;
    private final AdminFaqQueryRepository queryRepository;

    public PageResponse<FaqResponse> search(FaqSearchCondition cond, Pageable pageable) {
        return PageResponse.of(queryRepository.search(cond, pageable).map(FaqResponse::from));
    }

    public FaqResponse getDetail(Long id) {
        return FaqResponse.from(findFaq(id));
    }

    @Transactional
    public Long create(FaqRequest request) {
        return faqRepository.save(Faq.builder()
                .category(request.category())
                .question(request.question())
                .answer(request.answer())
                .build()).getId();
    }

    @Transactional
    public void update(Long id, FaqRequest request) {
        findFaq(id).update(request.category(), request.question(), request.answer());
    }

    @Transactional
    public void delete(Long id) {
        findFaq(id).delete();
    }

    private Faq findFaq(Long id) {
        return faqRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FAQ_NOT_FOUND));
    }
}
