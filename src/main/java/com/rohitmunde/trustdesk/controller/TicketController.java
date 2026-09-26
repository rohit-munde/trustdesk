package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.constants.MessageConstants;
import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.dto.UpdateTicketPriorityRequest;
import com.rohitmunde.trustdesk.dto.UpdateTicketStatusRequest;
import com.rohitmunde.trustdesk.response.ApiSuccessResponse;
import com.rohitmunde.trustdesk.service.TicketService;
import com.rohitmunde.trustdesk.service.TicketTriageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketTriageService ticketTriageService;

    @GetMapping("/{ticketId}")
    public ApiSuccessResponse<TicketDetailsDto> getTicketById(@PathVariable String ticketId) {
        TicketDetailsDto ticketDetailsDto = ticketService.getTicketById(ticketId);
        return new ApiSuccessResponse<>(MessageConstants.TICKET_FETCH_SUCCESS, ticketDetailsDto);
    }

    @GetMapping
    public ApiSuccessResponse<List<TicketDetailsDto>> getAllTickets() {
        List<TicketDetailsDto> tickets = ticketService.getAllTickets();
        return new ApiSuccessResponse<>(MessageConstants.TICKET_FETCH_SUCCESS, tickets);
    }

    @PatchMapping("/{ticketId}/status")
    public ApiSuccessResponse<TicketDetailsDto> updateTicketStatus(
            @PathVariable String ticketId,
            @Valid @RequestBody UpdateTicketStatusRequest request) {
        TicketDetailsDto ticketDetailsDto = ticketService.updateTicketStatus(ticketId, request.getStatus());
        return new ApiSuccessResponse<>(MessageConstants.TICKET_UPDATE_SUCCESS, ticketDetailsDto);
    }

    @PatchMapping("/{ticketId}/priority")
    public ApiSuccessResponse<TicketDetailsDto> updateTicketPriority(
            @PathVariable String ticketId,
            @Valid @RequestBody UpdateTicketPriorityRequest request) {
        TicketDetailsDto ticketDetailsDto = ticketService.updateTicketPriority(ticketId, request.getPriority());
        return new ApiSuccessResponse<>(MessageConstants.TICKET_UPDATE_SUCCESS, ticketDetailsDto);
    }

    @PostMapping("/{ticketId}/triage")
    public ApiSuccessResponse<TriageResult> triageTicket(@PathVariable String ticketId) {
        TriageResult triageResult = ticketTriageService.triageTicket(ticketId);
        return new ApiSuccessResponse<>(MessageConstants.TICKET_TRIAGE_SUCCESS, triageResult);
    }
}
