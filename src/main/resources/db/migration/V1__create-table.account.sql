CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,   
    description TEXT, 
    image_url VARCHAR(512),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE account_price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    months INT NOT NULL,
    type ENUM('Pantalla', 'Completa') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account(id)
);
