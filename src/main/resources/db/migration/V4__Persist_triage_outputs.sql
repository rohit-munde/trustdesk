ALTER TABLE tickets
    ADD COLUMN draft_reply TEXT,
    ADD COLUMN citations_json TEXT,
    ADD COLUMN recommended_action VARCHAR(255);