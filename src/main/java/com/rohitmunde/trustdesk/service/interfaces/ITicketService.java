package com.rohitmunde.trustdesk.service.interfaces;

import com.rohitmunde.trustdesk.dto.TicketDetailsDto;

import java.util.List;

public interface ITicketService {

    TicketDetailsDto getTicketById(String id);

    List<TicketDetailsDto> getAllTickets();
}
