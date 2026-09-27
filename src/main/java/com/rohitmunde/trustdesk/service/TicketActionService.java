package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.constants.MessageConstants;
import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.enums.ActionApprovalStatus;
import com.rohitmunde.trustdesk.exception.BusinessException;
import com.rohitmunde.trustdesk.exception.TicketNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketActionService {

    private static final String ACTION_NOT_PENDING = "Ticket action is not pending approval";

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    @Transactional
    public TicketDetailsDto approveAction(String ticketId) {
        Ticket ticket = findPendingActionTicket(ticketId);
        ticket.setActionApprovalStatus(ActionApprovalStatus.APPROVED);
        ticketRepository.save(ticket);
        return ticketService.toDetailsDto(ticket);
    }

    @Transactional
    public TicketDetailsDto rejectAction(String ticketId) {
        Ticket ticket = findPendingActionTicket(ticketId);
        ticket.setActionApprovalStatus(ActionApprovalStatus.REJECTED);
        ticketRepository.save(ticket);
        return ticketService.toDetailsDto(ticket);
    }

    private Ticket findPendingActionTicket(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format(MessageConstants.TICKET_NOT_FOUND, ticketId)));

        if (ticket.getActionApprovalStatus() != ActionApprovalStatus.PENDING_APPROVAL) {
            throw new BusinessException(ACTION_NOT_PENDING, HttpStatus.BAD_REQUEST);
        }

        return ticket;
    }
}
