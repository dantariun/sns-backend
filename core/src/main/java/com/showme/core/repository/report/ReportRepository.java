package com.showme.core.repository.report;

import com.showme.core.entity.report.Report;
import com.showme.core.entity.report.ReportTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(ReportTargetType targetType, Long targetId);

    long countByTargetTypeAndTargetId(ReportTargetType targetType, Long targetId);
}
