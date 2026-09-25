package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.constants.MessageConstants;
import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.dto.UpdateTicketStatusRequest;
import com.rohitmunde.trustdesk.enums.TicketStatus;
import com.rohitmunde.trustdesk.response.ApiSuccessResponse;
import com.rohitmunde.trustdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ApiSuccessResponse<TicketDetailsDto> getTicketById(@PathVariable String id) {
        TicketDetailsDto ticketDetailsDto = ticketService.getTicketById(id);
        return new ApiSuccessResponse<>(MessageConstants.TICKET_FETCH_SUCCESS, ticketDetailsDto);
    }

    @GetMapping
    public ApiSuccessResponse<List<TicketDetailsDto>> getAllTickets() {
        List<TicketDetailsDto> tickets = ticketService.getAllTickets();
        return new ApiSuccessResponse<>(MessageConstants.TICKET_FETCH_SUCCESS, tickets);
    }

    //Update ticket priority API
    @PutMapping("/{id}/priority")
    public ApiSuccessResponse<TicketDetailsDto> updateTicketPriority(@PathVariable String id, @Valid @RequestBody UpdateTicketStatusRequest request) {
        TicketDetailsDto ticketDetailsDto = ticketService.updateTicketPriority(id, request.getStatus());
        return new ApiSuccessResponse<>(MessageConstants.TICKET_UPDATE_SUCCESS, ticketDetailsDto);
    }
}
