CREATE TABLE ai_operation_traces (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id VARCHAR(255) NOT NULL,
    operation_type VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    input_summary TEXT,
    retrieved_policies TEXT,
    output_json TEXT,
    error_message TEXT,
    created_at DATETIME(6),
    PRIMARY KEY (id),
    INDEX idx_ai_operation_traces_ticket_id (ticket_id),
    CONSTRAINT fk_ai_operation_traces_ticket
        FOREIGN KEY (ticket_id)
            REFERENCES tickets (ticket_id) ON DELETE CASCADE
);