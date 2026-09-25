package com.rohitmunde.trustdesk.entity;

import com.rohitmunde.trustdesk.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

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

    @Column(name = "triaged_at")
    private OffsetDateTime triagedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory ticketCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority ticketPriority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketChannel ticketChannel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketSentiment ticketSentiment;

    @Column
    private Boolean escalationRequired;
}
