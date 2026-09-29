package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.dto.TriageEvaluationCaseResultDto;
import com.rohitmunde.trustdesk.dto.TriageEvaluationSummaryDto;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.service.TriageEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EvaluationControllerTest {

    private TriageEvaluationService triageEvaluationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        triageEvaluationService = mock(TriageEvaluationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new EvaluationController(triageEvaluationService))
                .build();
    }

    @Test
    void runsTriageEvaluation() throws Exception {
        when(triageEvaluationService.runSeededEvaluation())
                .thenReturn(TriageEvaluationSummaryDto.builder()
                        .totalCases(1)
                        .passedCases(1)
                        .failedCases(0)
                        .results(List.of(TriageEvaluationCaseResultDto.builder()
                                .ticketId("tkt_9001")
                                .passed(true)
                                .expectedCategory(TicketCategory.REFUND)
                                .actualCategory(TicketCategory.REFUND)
                                .expectedPriority(TicketPriority.MEDIUM)
                                .actualPriority(TicketPriority.MEDIUM)
                                .expectedSentiment(TicketSentiment.FRUSTRATED)
                                .actualSentiment(TicketSentiment.FRUSTRATED)
                                .expectedEscalationRequired(false)
                                .actualEscalationRequired(false)
                                .build()))
                        .build());

        mockMvc.perform(post("/evaluations/triage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.totalCases").value(1))
                .andExpect(jsonPath("$.payload.passedCases").value(1))
                .andExpect(jsonPath("$.payload.results[0].ticketId").value("tkt_9001"));
    }
}
