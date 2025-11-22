CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(190) UNIQUE NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS ride_request (
    id            BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    end_address   VARCHAR(255)    NOT NULL,
    end_point     POINT           NOT NULL,
    start_address VARCHAR(255)    NOT NULL,
    start_point   POINT           NOT NULL,
    status        ENUM('MATCHED', 'WAITING') NOT NULL,
    username      VARCHAR(255)    NOT NULL,
    user_id       BIGINT          NOT NULL,
    CONSTRAINT fk_ride_request_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);