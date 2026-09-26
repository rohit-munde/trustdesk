package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.constants.MessageConstants;
import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketStatus;
import com.rohitmunde.trustdesk.exception.TicketNotFoundException;
import com.rohitmunde.trustdesk.service.interfaces.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {

    private final TicketRepository ticketRepository;

    @Override
    public TicketDetailsDto getTicketById(String ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format(MessageConstants.TICKET_NOT_FOUND, ticketId)));

        return toDetailsDto(ticket);
    }

    @Override
    public List<TicketDetailsDto> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDetailsDto updateTicketStatus(String id, TicketStatus status) {
        Ticket ticket = findTicket(id);
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return toDetailsDto(ticket);
    }

    @Override
    public TicketDetailsDto updateTicketPriority(String id, TicketPriority priority) {
        Ticket ticket = findTicket(id);
        ticket.setTicketPriority(priority);
        ticketRepository.save(ticket);
        return toDetailsDto(ticket);
    }

    private Ticket findTicket(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(String.format(MessageConstants.TICKET_NOT_FOUND, id)));
    }

    private TicketDetailsDto toDetailsDto(Ticket ticket) {
        return TicketDetailsDto.builder()
                .ticketId(ticket.getId())
                .channel(ticket.getChannel())
                .subject(ticket.getSubject())
                .body(ticket.getBody())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .build();
    }
}
