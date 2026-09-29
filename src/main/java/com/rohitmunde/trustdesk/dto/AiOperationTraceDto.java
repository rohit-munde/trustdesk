package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.enums.AiOperationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class AiOperationTraceDto {
    private Long id;
    private String ticketId;
    private String operationType;
    private String provider;
    private AiOperationStatus status;
    private String inputSummary;
    private String retrievedPolicies;
    private String outputJson;
    private String errorMessage;
    private OffsetDateTime createdAt;
}
