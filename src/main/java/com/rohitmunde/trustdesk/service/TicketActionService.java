package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.constants.MessageConstants;
import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.enums.ActionApprovalStatus;
import com.rohitmunde.trustdesk.enums.ActionExecutionStatus;
import com.rohitmunde.trustdesk.enums.RecommendedAction;
import com.rohitmunde.trustdesk.exception.BusinessException;
import com.rohitmunde.trustdesk.exception.TicketNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TicketActionService {

    private static final String ACTION_NOT_PENDING = "Ticket action is not pending approval";
    private static final String ACTION_NOT_APPROVED = "Ticket action must be approved before execution";
    private static final String NO_EXECUTABLE_ACTION = "Ticket has no executable recommended action";

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
    public TicketDetailsDto executeAction(String ticketId) {
        Ticket ticket = findTicket(ticketId);

        if (ticket.getActionExecutionStatus() == ActionExecutionStatus.EXECUTED) {
            return ticketService.toDetailsDto(ticket);
        }

        if (ticket.getActionApprovalStatus() != ActionApprovalStatus.APPROVED) {
            throw new BusinessException(ACTION_NOT_APPROVED, HttpStatus.BAD_REQUEST);
        }

        if (ticket.getRecommendedAction() == null || ticket.getRecommendedAction() == RecommendedAction.NO_ACTION) {
            throw new BusinessException(NO_EXECUTABLE_ACTION, HttpStatus.BAD_REQUEST);
        }

        ticket.setActionExecutionStatus(ActionExecutionStatus.EXECUTED);
        ticket.setActionExecutionReference(buildExecutionReference(ticket));
        ticket.setActionExecutedAt(OffsetDateTime.now());
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
        Ticket ticket = findTicket(ticketId);

        if (ticket.getActionApprovalStatus() != ActionApprovalStatus.PENDING_APPROVAL) {
            throw new BusinessException(ACTION_NOT_PENDING, HttpStatus.BAD_REQUEST);
        }

        return ticket;
    }

    private Ticket findTicket(String ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format(MessageConstants.TICKET_NOT_FOUND, ticketId)));
    }

    private String buildExecutionReference(Ticket ticket) {
        return switch (ticket.getRecommendedAction()) {
            case CREATE_REPLACEMENT_ORDER -> "replacement_order:" + ticket.getId();
            case START_REFUND_REVIEW -> "refund_review:" + ticket.getId();
            case CHECK_SHIPPING_STATUS -> "shipping_check:" + ticket.getId();
            case REVIEW_BILLING_CHARGE -> "billing_review:" + ticket.getId();
            case REVIEW_WARRANTY_CLAIM -> "warranty_review:" + ticket.getId();
            case REVIEW_ACCOUNT_SECURITY -> "account_security_review:" + ticket.getId();
            case NO_ACTION -> throw new BusinessException(NO_EXECUTABLE_ACTION, HttpStatus.BAD_REQUEST);
        };
    }
}
