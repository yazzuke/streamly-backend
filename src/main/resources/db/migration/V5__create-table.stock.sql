CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type ENUM('Pantalla', 'Completa') NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_assigned BOOLEAN DEFAULT FALSE,
    assigned_user_id BIGINT,
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_id BIGINT NOT NULL,
    profile_name VARCHAR(255) NOT NULL,
    profile_password VARCHAR(255) NOT NULL,
    is_assigned BOOLEAN DEFAULT FALSE,
    assigned_user_id BIGINT,
    FOREIGN KEY (stock_id) REFERENCES stock(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_user_id) REFERENCES users(id) ON DELETE SET NULL
);
