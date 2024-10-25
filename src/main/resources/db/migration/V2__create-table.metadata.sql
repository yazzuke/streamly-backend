CREATE TABLE service_metadata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    svg_url VARCHAR(512)
);
