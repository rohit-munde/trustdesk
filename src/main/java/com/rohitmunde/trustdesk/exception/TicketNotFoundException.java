package com.rohitmunde.trustdesk.exception;

public class TicketNotFoundException extends ResourceNotFoundException {

    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException() {
        super("Ticket not found");
    }
}
