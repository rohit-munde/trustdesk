CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tier VARCHAR(255),
    country VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    verified BOOLEAN
);

CREATE TABLE IF NOT EXISTS customer_tags (
    customer_id VARCHAR(50) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    CONSTRAINT fk_customer_tags_customer
        FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    status VARCHAR(255),
    placed_at DATETIME(6) NOT NULL,
    delivered_at DATETIME(6),
    eligible_return_until DATETIME(6),
    total DOUBLE NOT NULL,
    currency VARCHAR(255) NOT NULL,
    payment_status VARCHAR(255) NOT NULL,
    tracking_number VARCHAR(255),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER,
    category VARCHAR(255),
    final_sale BOOLEAN NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

CREATE TABLE IF NOT EXISTS tickets (
    ticket_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    order_id VARCHAR(50) NOT NULL,
    channel VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    status VARCHAR(255) NOT NULL,
    triaged_at DATETIME(6),
    ticket_category VARCHAR(255) NOT NULL,
    ticket_priority VARCHAR(255) NOT NULL,
    ticket_sentiment VARCHAR(255) NOT NULL,
    escalation_required BOOLEAN,
    CONSTRAINT fk_tickets_customer
        FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    CONSTRAINT fk_tickets_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id)
);
