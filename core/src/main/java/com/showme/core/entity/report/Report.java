package com.showme.core.entity.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReportTargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    private Long reporterId;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReportStatus status = ReportStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ReportAction processedAction;

    @Column(length = 500)
    private String processedMemo;

    private Long processedBy;

    private LocalDateTime processedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Report(ReportTargetType targetType, Long targetId, Long reporterId, String reason) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.reporterId = reporterId;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }

    public boolean isPending() { return this.status == ReportStatus.PENDING; }

    public void process(ReportAction action, String memo, Long adminId) {
        this.status = action == ReportAction.DISMISS ? ReportStatus.DISMISSED : ReportStatus.PROCESSED;
        this.processedAction = action;
        this.processedMemo = memo;
        this.processedBy = adminId;
        this.processedAt = LocalDateTime.now();
    }
}
