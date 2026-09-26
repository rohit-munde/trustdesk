package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.entity.Customer;
import com.rohitmunde.trustdesk.entity.Order;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketChannel;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.enums.TicketStatus;
import com.rohitmunde.trustdesk.exception.TicketNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketServiceTest {

    private final TicketRepository ticketRepository = mock(TicketRepository.class);
    private final TicketService ticketService = new TicketService(ticketRepository);

    @Test
    void getsAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket("tkt_9001")));

        var tickets = ticketService.getAllTickets();

        assertThat(tickets).hasSize(1);
        assertThat(tickets.getFirst().getTicketId()).isEqualTo("tkt_9001");
    }

    @Test
    void getsTicketById() {
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket("tkt_9001")));

        var ticket = ticketService.getTicketById("tkt_9001");

        assertThat(ticket.getTicketId()).isEqualTo("tkt_9001");
    }

    @Test
    void mapsTicketContextAndTriageFields() {
        Ticket ticket = ticket("tkt_9001");
        OffsetDateTime triagedAt = OffsetDateTime.parse("2026-06-28T11:15:00+05:30");
        ticket.setTicketPriority(TicketPriority.HIGH);
        ticket.setTicketCategory(TicketCategory.REFUND);
        ticket.setTicketSentiment(TicketSentiment.FRUSTRATED);
        ticket.setEscalationRequired(true);
        ticket.setTriagedAt(triagedAt);
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket));

        var response = ticketService.getTicketById("tkt_9001");

        assertThat(response.getCustomerId()).isEqualTo("cus_1001");
        assertThat(response.getOrderId()).isEqualTo("ord_5001");
        assertThat(response.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(response.getCategory()).isEqualTo(TicketCategory.REFUND);
        assertThat(response.getSentiment()).isEqualTo(TicketSentiment.FRUSTRATED);
        assertThat(response.getEscalationRequired()).isTrue();
        assertThat(response.getTriagedAt()).isEqualTo(triagedAt);
    }

    @Test
    void updatesStatusWithoutChangingPriority() {
        Ticket ticket = ticket("tkt_9001");
        ticket.setTicketPriority(TicketPriority.LOW);
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket));

        ticketService.updateTicketStatus("tkt_9001", TicketStatus.IN_PROGRESS);

        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
        assertThat(ticket.getTicketPriority()).isEqualTo(TicketPriority.LOW);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void updatesPriorityWithoutChangingStatus() {
        Ticket ticket = ticket("tkt_9001");
        ticket.setStatus(TicketStatus.OPEN);
        when(ticketRepository.findById("tkt_9001")).thenReturn(Optional.of(ticket));

        ticketService.updateTicketPriority("tkt_9001", TicketPriority.HIGH);

        assertThat(ticket.getTicketPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.OPEN);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void missingTicketThrowsNotFound() {
        when(ticketRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById("missing"))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Ticket not found with id: missing");
    }

    private Ticket ticket(String id) {
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
        ticket.setTicketPriority(TicketPriority.LOW);
        return ticket;
    }
}
