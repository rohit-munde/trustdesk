ALTER TABLE tickets
    ADD COLUMN action_execution_status VARCHAR(255),
    ADD COLUMN action_execution_reference VARCHAR(255),
    ADD COLUMN action_executed_at DATETIME(6);