ALTER TABLE post
    ADD COLUMN visibility VARCHAR(10) NOT NULL DEFAULT 'PUBLIC' AFTER content,
    DROP COLUMN thumbnail_url;

CREATE TABLE post_media (
    post_id    BIGINT       NOT NULL,
    sort_order INT          NOT NULL,
    url        VARCHAR(500) NOT NULL,
    PRIMARY KEY (post_id, sort_order)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE post_tag (
    post_id BIGINT      NOT NULL,
    tag     VARCHAR(50) NOT NULL,
    PRIMARY KEY (post_id, tag),
    KEY idx_post_tag_tag (tag)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE post_comment (
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    post_id    BIGINT        NOT NULL,
    member_id  BIGINT        NOT NULL,
    parent_id  BIGINT,
    content    VARCHAR(1000) NOT NULL,
    like_count INT           NOT NULL DEFAULT 0,
    blinded    BIT(1)        NOT NULL DEFAULT 0,
    deleted_at DATETIME(6),
    created_at DATETIME(6)   NOT NULL,
    updated_at DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    KEY idx_post_comment_post_id (post_id, created_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE post_like (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    post_id    BIGINT      NOT NULL,
    member_id  BIGINT      NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_like (post_id, member_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE report (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    target_type      VARCHAR(10)  NOT NULL,
    target_id        BIGINT       NOT NULL,
    reporter_id      BIGINT       NOT NULL,
    reason           VARCHAR(500) NOT NULL,
    status           VARCHAR(10)  NOT NULL DEFAULT 'PENDING',
    processed_action VARCHAR(10),
    processed_memo   VARCHAR(500),
    processed_by     BIGINT,
    processed_at     DATETIME(6),
    created_at       DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_report_target (target_type, target_id),
    KEY idx_report_status (status, created_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE notice (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    title      VARCHAR(200) NOT NULL,
    content    TEXT         NOT NULL,
    published  BIT(1)       NOT NULL,
    admin_id   BIGINT       NOT NULL,
    deleted_at DATETIME(6),
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE faq (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    category   VARCHAR(10)  NOT NULL,
    question   VARCHAR(300) NOT NULL,
    answer     TEXT         NOT NULL,
    deleted_at DATETIME(6),
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_faq_category (category)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
