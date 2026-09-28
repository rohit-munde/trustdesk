package com.rohitmunde.trustdesk.entity;

import com.rohitmunde.trustdesk.enums.AiOperationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ai_operation_traces")
public class AiOperationTrace {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AiOperationStatus status;

    @Lob
    @Column(name = "input_summary", columnDefinition = "TEXT")
    private String inputSummary;

    @Lob
    @Column(name = "retrieved_policies", columnDefinition = "TEXT")
    private String retrievedPolicies;

    @Lob
    @Column(name = "output_json", columnDefinition = "TEXT")
    private String outputJson;

    @Lob
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
