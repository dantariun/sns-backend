package com.showme.core.repository.faq;

import com.showme.core.entity.faq.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    Optional<Faq> findByIdAndDeletedAtIsNull(Long id);
}
