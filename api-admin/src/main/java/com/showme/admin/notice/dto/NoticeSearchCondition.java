package com.showme.admin.notice.dto;

/** keyword 는 제목 부분 일치, published 를 생략하면 게시·미게시 모두 */
public record NoticeSearchCondition(String keyword, Boolean published) {
}
