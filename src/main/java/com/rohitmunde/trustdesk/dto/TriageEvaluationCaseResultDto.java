package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TriageEvaluationCaseResultDto {
    private String ticketId;
    private boolean passed;
    private TicketCategory expectedCategory;
    private TicketCategory actualCategory;
    private TicketPriority expectedPriority;
    private TicketPriority actualPriority;
    private TicketSentiment expectedSentiment;
    private TicketSentiment actualSentiment;
    private Boolean expectedEscalationRequired;
    private Boolean actualEscalationRequired;
    private String errorMessage;
}
