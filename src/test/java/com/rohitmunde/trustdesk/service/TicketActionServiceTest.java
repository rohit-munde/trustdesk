package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.entity.Customer;
import com.rohitmunde.trustdesk.entity.Order;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.enums.ActionApprovalStatus;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketChannel;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.enums.TicketStatus;
import com.rohitmunde.trustdesk.exception.BusinessException;
import com.rohitmunde.trustdesk.exception.TicketNotFoundException;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketActionServiceTest {

    private final TicketRepository ticketRepository = mock(TicketRepository.class);
    private final TicketService ticketService = new TicketService(ticketRepository, new ObjectMapper());
    private final TicketActionService ticketActionService = new TicketActionService(ticketRepository, ticketService);

    @Test
    void approvesPendingAction() {
        Ticket ticket = ticket("tkt_9001", ActionApprovalStatus.PENDING_APPROVAL);
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket));

        var response = ticketActionService.approveAction("tkt_9001");

        assertThat(ticket.getActionApprovalStatus()).isEqualTo(ActionApprovalStatus.APPROVED);
        assertThat(response.getActionApprovalStatus()).isEqualTo(ActionApprovalStatus.APPROVED);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void rejectsPendingAction() {
        Ticket ticket = ticket("tkt_9001", ActionApprovalStatus.PENDING_APPROVAL);
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket));

        var response = ticketActionService.rejectAction("tkt_9001");

        assertThat(ticket.getActionApprovalStatus()).isEqualTo(ActionApprovalStatus.REJECTED);
        assertThat(response.getActionApprovalStatus()).isEqualTo(ActionApprovalStatus.REJECTED);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void rejectsNonPendingActionDecision() {
        Ticket ticket = ticket("tkt_9001", ActionApprovalStatus.APPROVED);
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> ticketActionService.approveAction("tkt_9001"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Ticket action is not pending approval");
    }

    @Test
    void missingTicketThrowsNotFound() {
        when(ticketRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketActionService.approveAction("missing"))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Ticket not found with id: missing");
    }

    private Ticket ticket(String id, ActionApprovalStatus actionApprovalStatus) {
        Customer customer = new Customer();
        customer.setId("cus_1001");

        Order order = new Order();
        order.setId("ord_5001");

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setCustomer(customer);
        ticket.setOrder(order);
        ticket.setChannel(TicketChannel.EMAIL);
        ticket.setSubject("Received damaged earbuds");
        ticket.setBody("The left earbud arrived cracked.");
        ticket.setCreatedAt(OffsetDateTime.parse("2026-06-28T10:15:00+05:30"));
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setTicketPriority(TicketPriority.MEDIUM);
        ticket.setTicketCategory(TicketCategory.REFUND);
        ticket.setTicketSentiment(TicketSentiment.FRUSTRATED);
        ticket.setEscalationRequired(false);
        ticket.setActionApprovalStatus(actionApprovalStatus);
        return ticket;
    }
}
