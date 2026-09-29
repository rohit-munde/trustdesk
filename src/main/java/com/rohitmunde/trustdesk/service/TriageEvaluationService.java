package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.dto.TriageEvaluationCaseResultDto;
import com.rohitmunde.trustdesk.dto.TriageEvaluationSummaryDto;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TriageEvaluationService {

    private final TicketTriageService ticketTriageService;

    public TriageEvaluationSummaryDto runSeededEvaluation() {
        List<ExpectedTriage> cases = List.of(
                new ExpectedTriage("tkt_9001", TicketCategory.REFUND, TicketPriority.MEDIUM, TicketSentiment.FRUSTRATED, false),
                new ExpectedTriage("tkt_9002", TicketCategory.SHIPPING, TicketPriority.LOW, TicketSentiment.NEUTRAL, false)
        );

        List<TriageEvaluationCaseResultDto> results = cases.stream()
                .map(this::evaluate)
                .toList();
        int passed = (int) results.stream().filter(TriageEvaluationCaseResultDto::isPassed).count();

        return TriageEvaluationSummaryDto.builder()
                .totalCases(results.size())
                .passedCases(passed)
                .failedCases(results.size() - passed)
                .results(results)
                .build();
    }

    private TriageEvaluationCaseResultDto evaluate(ExpectedTriage expected) {
        try {
            TriageResult actual = ticketTriageService.triageTicket(expected.ticketId());
            boolean passed = expected.category() == actual.getCategory()
                    && expected.priority() == actual.getPriority()
                    && expected.sentiment() == actual.getSentiment()
                    && expected.escalationRequired().equals(actual.getEscalationRequired());

            return TriageEvaluationCaseResultDto.builder()
                    .ticketId(expected.ticketId())
                    .passed(passed)
                    .expectedCategory(expected.category())
                    .actualCategory(actual.getCategory())
                    .expectedPriority(expected.priority())
                    .actualPriority(actual.getPriority())
                    .expectedSentiment(expected.sentiment())
                    .actualSentiment(actual.getSentiment())
                    .expectedEscalationRequired(expected.escalationRequired())
                    .actualEscalationRequired(actual.getEscalationRequired())
                    .build();
        } catch (Exception ex) {
            return TriageEvaluationCaseResultDto.builder()
                    .ticketId(expected.ticketId())
                    .passed(false)
                    .expectedCategory(expected.category())
                    .expectedPriority(expected.priority())
                    .expectedSentiment(expected.sentiment())
                    .expectedEscalationRequired(expected.escalationRequired())
                    .errorMessage(ex.getMessage())
                    .build();
        }
    }

    private record ExpectedTriage(
            String ticketId,
            TicketCategory category,
            TicketPriority priority,
            TicketSentiment sentiment,
            Boolean escalationRequired
    ) {
    }
}
