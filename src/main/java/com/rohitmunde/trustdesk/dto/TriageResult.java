package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TriageResult {
    private TicketCategory category;
    private TicketPriority priority;
    private TicketSentiment sentiment;
    private Boolean escalationRequired;
    private List<String> citations;
}
