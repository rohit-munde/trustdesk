package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketChannel;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.enums.TicketStatus;
import com.rohitmunde.trustdesk.exception.GlobalExceptionHandler;
import com.rohitmunde.trustdesk.exception.TicketNotFoundException;
import com.rohitmunde.trustdesk.service.TicketService;
import com.rohitmunde.trustdesk.service.TicketTriageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketControllerTest {

    private TicketService ticketService;
    private TicketTriageService ticketTriageService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);
        ticketTriageService = mock(TicketTriageService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TicketController(ticketService, ticketTriageService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listsTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(List.of(ticket("tkt_9001")));

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload[0].ticketId").value("tkt_9001"));
    }

    @Test
    void returnsTicketDetails() throws Exception {
        when(ticketService.getTicketById("tkt_9001")).thenReturn(ticket("tkt_9001"));

        mockMvc.perform(get("/tickets/tkt_9001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.ticketId").value("tkt_9001"));
    }

    @Test
    void updatesTicketStatus() throws Exception {
        when(ticketService.updateTicketStatus("tkt_9001", TicketStatus.IN_PROGRESS))
                .thenReturn(ticket("tkt_9001"));

        mockMvc.perform(patch("/tickets/tkt_9001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updatesTicketPriority() throws Exception {
        when(ticketService.updateTicketPriority("tkt_9001", TicketPriority.HIGH))
                .thenReturn(ticket("tkt_9001"));

        mockMvc.perform(patch("/tickets/tkt_9001/priority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"priority\":\"HIGH\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void triagesTicket() throws Exception {
        when(ticketTriageService.triageTicket("tkt_9001")).thenReturn(TriageResult.builder()
                .category(TicketCategory.REFUND)
                .priority(TicketPriority.MEDIUM)
                .sentiment(TicketSentiment.FRUSTRATED)
                .escalationRequired(false)
                .citations(List.of("refund_policy.md"))
                .build());

        mockMvc.perform(post("/tickets/tkt_9001/triage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.category").value("REFUND"))
                .andExpect(jsonPath("$.payload.citations[0]").value("refund_policy.md"));
    }

    @Test
    void invalidEnumValueReturnsBadRequest() throws Exception {
        mockMvc.perform(patch("/tickets/tkt_9001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"BOUNCED\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value 'BOUNCED'. Allowed values are: [OPEN, IN_PROGRESS, RESOLVED, CLOSED]"));
    }

    @Test
    void missingTicketReturnsNotFound() throws Exception {
        when(ticketService.getTicketById("missing"))
                .thenThrow(new TicketNotFoundException("Ticket not found with id: missing"));

        mockMvc.perform(get("/tickets/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket not found with id: missing"));
    }

    private TicketDetailsDto ticket(String id) {
        return TicketDetailsDto.builder()
                .ticketId(id)
                .channel(TicketChannel.EMAIL)
                .subject("Received damaged earbuds")
                .body("The left earbud arrived cracked.")
                .createdAt(OffsetDateTime.parse("2026-06-28T10:15:00+05:30"))
                .status(TicketStatus.OPEN)
                .build();
    }
}
