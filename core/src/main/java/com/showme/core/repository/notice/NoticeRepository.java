package com.showme.core.repository.notice;

import com.showme.core.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);
}
