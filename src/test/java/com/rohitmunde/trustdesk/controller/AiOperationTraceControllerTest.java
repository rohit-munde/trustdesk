package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.dto.AiOperationTraceDto;
import com.rohitmunde.trustdesk.enums.AiOperationStatus;
import com.rohitmunde.trustdesk.service.AiOperationTraceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AiOperationTraceControllerTest {

    private AiOperationTraceService aiOperationTraceService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        aiOperationTraceService = mock(AiOperationTraceService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AiOperationTraceController(aiOperationTraceService))
                .build();
    }

    @Test
    void returnsTicketTraces() throws Exception {
        when(aiOperationTraceService.getTicketTraces("tkt_9001"))
                .thenReturn(List.of(AiOperationTraceDto.builder()
                        .id(1L)
                        .ticketId("tkt_9001")
                        .operationType("TICKET_TRIAGE")
                        .provider("openai")
                        .status(AiOperationStatus.SUCCESS)
                        .createdAt(OffsetDateTime.parse("2026-09-28T15:49:14.459689Z"))
                        .build()));

        mockMvc.perform(get("/tickets/tkt_9001/traces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload[0].ticketId").value("tkt_9001"))
                .andExpect(jsonPath("$.payload[0].provider").value("openai"))
                .andExpect(jsonPath("$.payload[0].status").value("SUCCESS"));
    }
}
