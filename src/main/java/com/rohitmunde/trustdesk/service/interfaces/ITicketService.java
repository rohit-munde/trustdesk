package com.rohitmunde.trustdesk.service.interfaces;

import com.rohitmunde.trustdesk.dto.TicketDetailsDto;

public interface ITicketService {

    TicketDetailsDto getTicketById(String id);
}
