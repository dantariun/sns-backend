ALTER TABLE member ADD COLUMN suspended_until DATETIME(6) NULL AFTER status;

CREATE TABLE member_sanction (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    member_id       BIGINT       NOT NULL,
    type            VARCHAR(20)  NOT NULL,
    reason          VARCHAR(500),
    suspended_until DATETIME(6),
    admin_id        BIGINT       NOT NULL,
    created_at      DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_member_sanction_member_id (member_id, created_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE follow (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    follower_id  BIGINT      NOT NULL,
    following_id BIGINT      NOT NULL,
    created_at   DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_follow_pair (follower_id, following_id),
    KEY idx_follow_following_id (following_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE post (
    id            BIGINT        NOT NULL AUTO_INCREMENT,
    member_id     BIGINT        NOT NULL,
    content       VARCHAR(2000),
    thumbnail_url VARCHAR(500),
    like_count    INT           NOT NULL DEFAULT 0,
    comment_count INT           NOT NULL DEFAULT 0,
    blinded       BIT(1)        NOT NULL DEFAULT 0,
    deleted_at    DATETIME(6),
    created_at    DATETIME(6)   NOT NULL,
    updated_at    DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    KEY idx_post_member_id (member_id, created_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
