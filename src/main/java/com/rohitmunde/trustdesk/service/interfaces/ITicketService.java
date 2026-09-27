package com.rohitmunde.trustdesk.service.interfaces;

import com.rohitmunde.trustdesk.dto.TicketDetailsDto;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketStatus;

import java.util.List;

public interface ITicketService {

    TicketDetailsDto getTicketById(String id);

    List<TicketDetailsDto> getAllTickets();

    TicketDetailsDto updateTicketStatus(String id, TicketStatus status);

    TicketDetailsDto updateTicketPriority(String id, TicketPriority priority);
}
