package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.enums.*;
import lombok.*;

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
@Builder
public class TicketDetailsDto {
    private String ticketId;
    private String customerId;
    private String orderId;
    private TicketChannel channel;
    private String subject;
    private String body;
    private OffsetDateTime createdAt;
    private TicketStatus status;
    private TicketPriority priority;
    private TicketCategory category;
    private TicketSentiment sentiment;
    private Boolean escalationRequired;
    private OffsetDateTime triagedAt;
    private String draftReply;
    private List<String> citations;
    private RecommendedAction recommendedAction;
    private ActionApprovalStatus actionApprovalStatus;
}
