CREATE DATABASE IF NOT EXISTS pahana_edu;
USE pahana_edu;

CREATE TABLE users
(
    user_id       INT PRIMARY KEY AUTO_INCREMENT,
    email         VARCHAR(100) UNIQUE                  NOT NULL,
    password      VARCHAR(255)                         NOT NULL,
    first_name    VARCHAR(100)                         NOT NULL,
    last_name     VARCHAR(100)                         NOT NULL,
    role          ENUM ('ADMIN', 'CASHIER', 'MANAGER') NOT NULL DEFAULT 'CASHIER',
    last_login_at TIMESTAMP                            NULL,
    created_at    TIMESTAMP                                     DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP                            NULL
);

CREATE TABLE customers
(
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100)        NOT NULL,
    address     TEXT                NOT NULL,
    telephone   VARCHAR(15)         NOT NULL,
    email       VARCHAR(100) UNIQUE NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP           NULL
);

CREATE TABLE categories
(
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP   NULL
);

CREATE TABLE items
(
    item_id     INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    category_id INT,
    unit_price  DECIMAL(10, 2) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP      NULL,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE TABLE bills
(
    bill_id        INT PRIMARY KEY AUTO_INCREMENT,
    customer_id    INT            NOT NULL,
    user_id        INT            NOT NULL,
    total_amount   DECIMAL(10, 2) NOT NULL,
    payment_status ENUM ('PENDING', 'PAID', 'CANCELLED')                       DEFAULT 'PENDING',
    payment_method ENUM ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER') DEFAULT 'CASH',
    notes          TEXT,
    created_at     TIMESTAMP                                                   DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP                                                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP      NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE bill_items
(
    bill_item_id INT PRIMARY KEY AUTO_INCREMENT,
    bill_id      INT            NOT NULL,
    item_id      INT            NOT NULL,
    quantity     INT            NOT NULL,
    unit_price   DECIMAL(10, 2) NOT NULL,
    line_total   DECIMAL(10, 2) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP      NULL,
    FOREIGN KEY (bill_id) REFERENCES bills (bill_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items (item_id)
);
