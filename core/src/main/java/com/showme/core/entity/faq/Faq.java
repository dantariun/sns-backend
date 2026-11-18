package com.showme.core.entity.faq;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "faq")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FaqCategory category;

    @Column(nullable = false, length = 300)
    private String question;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    private LocalDateTime deletedAt;

    @Builder
    public Faq(FaqCategory category, String question, String answer) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    public boolean isDeleted() { return this.deletedAt != null; }

    public void update(FaqCategory category, String question, String answer) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    public void delete() { this.deletedAt = LocalDateTime.now(); }
}
