package com.rohitmunde.trustdesk.entity;

import com.rohitmunde.trustdesk.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * {
 *     "ticket_id": "tkt_9001",
 *     "customer_id": "cus_1001",
 *     "order_id": "ord_5001",
 *     "channel": "email",
 *     "subject": "Received damaged earbuds",
 *     "body": "Hi, my BlueBuds Air arrived with the left earbud cracked. The package was delivered on June 24. Can I get a replacement?",
 *     "created_at": "2026-06-28T10:15:00+05:30",
 *     "status": "open",
 *     "expected_category": "refund",
 *     "expected_priority": "medium",
 *     "expected_sentiment": "frustrated",
 *     "expected_escalation": false,
 *     "expected_actions": ["create_replacement_order"]
 *   }
 * */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "ticket_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketChannel channel;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "expected_category", nullable = false)
//    private TicketCategory expectedCategory;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "expected_priority", nullable = false)
//    private TicketPriority expectedPriority;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "expected_sentiment", nullable = false)
//    private TicketSentiment expectedSentiment;
//
//    @Column(name = "expected_escalation", nullable = false)
//    private Boolean expectedEscalation;
//
//    @ElementCollection
//    @Column(name = "expected_actions")
//    private List<String> expectedActions;

    @Column(name = "triaged_at")
    private OffsetDateTime triagedAt;
}
