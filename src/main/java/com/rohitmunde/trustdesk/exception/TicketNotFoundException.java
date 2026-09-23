package com.rohitmunde.trustdesk.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends BusinessException {

    public TicketNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

    public TicketNotFoundException(){
        super("Ticket not found", HttpStatus.NOT_FOUND);
    }
}
