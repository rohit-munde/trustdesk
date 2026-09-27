INSERT IGNORE INTO customers (customer_id, name, email, tier, country, created_at, verified)
VALUES
    ('cus_1001', 'Aisha Rao', 'aisha.rao@example.com', 'GOLD', 'IN', '2024-05-14 00:00:00', TRUE),
    ('cus_1002', 'Rohan Mehta', 'rohan.mehta@example.com', 'STANDARD', 'IN', '2025-01-10 00:00:00', TRUE);

INSERT IGNORE INTO customer_tags (customer_id, tag)
VALUES
    ('cus_1001', 'loyal_customer'),
    ('cus_1001', 'high_lifetime_value'),
    ('cus_1002', 'new_customer');

INSERT IGNORE INTO orders (order_id, customer_id, status, placed_at, delivered_at, eligible_return_until, total, currency, payment_status, tracking_number)
VALUES
    ('ord_5001', 'cus_1001', 'DELIVERED', '2026-06-20 00:00:00', '2026-06-24 00:00:00', '2026-07-01 00:00:00', 8999, 'INR', 'PAID', 'BLUETRK10001'),
    ('ord_5002', 'cus_1002', 'SHIPPED', '2026-07-05 00:00:00', NULL, NULL, 2499, 'INR', 'PAID', 'BLUETRK10002');

INSERT IGNORE INTO order_items (order_id, sku, name, quantity, category, final_sale)
VALUES
    ('ord_5001', 'BG-AIRPODS-01', 'BlueBuds Air', 1, 'audio', FALSE),
    ('ord_5002', 'CASE-PIXEL-01', 'Pixel Case', 1, 'accessories', FALSE);

INSERT IGNORE INTO tickets (ticket_id, customer_id, order_id, channel, subject, body, created_at, status, triaged_at, ticket_category, ticket_priority, ticket_sentiment, escalation_required)
VALUES
    ('tkt_9001', 'cus_1001', 'ord_5001', 'EMAIL', 'Received damaged earbuds', 'Hi, my BlueBuds Air arrived with the left earbud cracked. The package was delivered on June 24. Can I get a replacement?', '2026-06-28 10:15:00', 'OPEN', NULL, 'REFUND', 'MEDIUM', 'FRUSTRATED', FALSE),
    ('tkt_9002', 'cus_1002', 'ord_5002', 'CHAT', 'Where is my order?', 'The tracking link has not updated for two days. Can you check the order status?', '2026-07-07 14:30:00', 'OPEN', NULL, 'SHIPPING', 'LOW', 'NEUTRAL', FALSE);
