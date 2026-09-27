package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.enums.ActionApprovalStatus;
import com.rohitmunde.trustdesk.enums.RecommendedAction;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketChannel;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.enums.TicketStatus;
import com.rohitmunde.trustdesk.exception.GlobalExceptionHandler;
import com.rohitmunde.trustdesk.exception.BusinessException;
import com.rohitmunde.trustdesk.service.TicketActionService;
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
    private TicketActionService ticketActionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);
        ticketTriageService = mock(TicketTriageService.class);
        ticketActionService = mock(TicketActionService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TicketController(ticketService, ticketTriageService, ticketActionService))
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
                .andExpect(jsonPath("$.payload.ticketId").value("tkt_9001"))
                .andExpect(jsonPath("$.payload.customerId").value("cus_1001"))
                .andExpect(jsonPath("$.payload.orderId").value("ord_5001"))
                .andExpect(jsonPath("$.payload.priority").value("LOW"))
                .andExpect(jsonPath("$.payload.category").value("GENERAL"))
                .andExpect(jsonPath("$.payload.sentiment").value("NEUTRAL"))
                .andExpect(jsonPath("$.payload.escalationRequired").value(false))
                .andExpect(jsonPath("$.payload.triagedAt").value("2026-06-28T11:15:00+05:30"))
                .andExpect(jsonPath("$.payload.actionApprovalStatus").value("PENDING_APPROVAL"));
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
                .draftReply("I'm sorry your item arrived damaged. Based on our refund and replacement policy, we can help review this for a refund or replacement.")
                .recommendedAction(RecommendedAction.CREATE_REPLACEMENT_ORDER)
                .build());

        mockMvc.perform(post("/tickets/tkt_9001/triage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.category").value("REFUND"))
                .andExpect(jsonPath("$.payload.citations[0]").value("refund_policy.md"))
                .andExpect(jsonPath("$.payload.draftReply").isNotEmpty())
                .andExpect(jsonPath("$.payload.recommendedAction").value("CREATE_REPLACEMENT_ORDER"));
    }

    @Test
    void approvesTicketAction() throws Exception {
        when(ticketActionService.approveAction("tkt_9001"))
                .thenReturn(ticket("tkt_9001", ActionApprovalStatus.APPROVED));

        mockMvc.perform(post("/tickets/tkt_9001/actions/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.actionApprovalStatus").value("APPROVED"));
    }

    @Test
    void rejectsTicketAction() throws Exception {
        when(ticketActionService.rejectAction("tkt_9001"))
                .thenReturn(ticket("tkt_9001", ActionApprovalStatus.REJECTED));

        mockMvc.perform(post("/tickets/tkt_9001/actions/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.actionApprovalStatus").value("REJECTED"));
    }

    @Test
    void actionDecisionReturnsBadRequestWhenNotPending() throws Exception {
        when(ticketActionService.approveAction("tkt_9001"))
                .thenThrow(new BusinessException("Ticket action is not pending approval", org.springframework.http.HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/tickets/tkt_9001/actions/approve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ticket action is not pending approval"));
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
        return ticket(id, ActionApprovalStatus.PENDING_APPROVAL);
    }

    private TicketDetailsDto ticket(String id, ActionApprovalStatus actionApprovalStatus) {
        return TicketDetailsDto.builder()
                .ticketId(id)
                .channel(TicketChannel.EMAIL)
                .subject("Received damaged earbuds")
                .body("The left earbud arrived cracked.")
                .createdAt(OffsetDateTime.parse("2026-06-28T10:15:00+05:30"))
                .status(TicketStatus.OPEN)
                .customerId("cus_1001")
                .orderId("ord_5001")
                .priority(TicketPriority.LOW)
                .category(TicketCategory.GENERAL)
                .sentiment(TicketSentiment.NEUTRAL)
                .escalationRequired(false)
                .triagedAt(OffsetDateTime.parse("2026-06-28T11:15:00+05:30"))
                .actionApprovalStatus(actionApprovalStatus)
                .build();
    }
}
