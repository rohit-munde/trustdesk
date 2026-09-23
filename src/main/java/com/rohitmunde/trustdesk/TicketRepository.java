package com.rohitmunde.trustdesk;

import com.rohitmunde.trustdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}
