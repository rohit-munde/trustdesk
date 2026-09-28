package com.rohitmunde.trustdesk.repository;

import com.rohitmunde.trustdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}
