CREATE TABLE member (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    login_id                VARCHAR(100) NOT NULL,
    nickname                VARCHAR(20)  NOT NULL,
    real_name               VARCHAR(20),
    password_hash           VARCHAR(100) NOT NULL,
    phone_no                VARCHAR(20),
    bio                     VARCHAR(100),
    profile_image_url       VARCHAR(200),
    push_token              VARCHAR(255),
    status                  VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
    last_login_at           DATETIME(6),
    withdrawal_requested_at DATETIME(6),
    deleted_at              DATETIME(6),
    created_at              DATETIME(6)  NOT NULL,
    updated_at              DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_login_id (login_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE admin_member (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    login_id      VARCHAR(50)  NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    name          VARCHAR(30)  NOT NULL,
    role          VARCHAR(10)  NOT NULL,
    active        BIT(1)       NOT NULL DEFAULT 1,
    last_login_at DATETIME(6),
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_member_login_id (login_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE admin_refresh_token (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    admin_id   BIGINT       NOT NULL,
    token      VARCHAR(500) NOT NULL,
    expires_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_refresh_token_token (token),
    KEY idx_admin_refresh_token_admin_id (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
