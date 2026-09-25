package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.constants.MessageConstants;
import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.entity.Ticket;
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

        return TicketDetailsDto.builder()
                .ticketId(ticket.getId())
                .channel(ticket.getChannel())
                .subject(ticket.getSubject())
                .body(ticket.getBody())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

    @Override
    public List<TicketDetailsDto> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticket -> TicketDetailsDto.builder()
                        .ticketId(ticket.getId())
                        .channel(ticket.getChannel())
                        .subject(ticket.getSubject())
                        .body(ticket.getBody())
                        .status(ticket.getStatus())
                        .createdAt(ticket.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public TicketDetailsDto updateTicketPriority(String id, TicketStatus ticketPriority) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(String.format(MessageConstants.TICKET_NOT_FOUND, id)));

        ticket.setStatus(ticketPriority);
        ticketRepository.save(ticket);
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
