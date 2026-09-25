package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.ai.AiClient;
import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.model.KnowledgeDocument;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TicketTriageService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private AiClient aiClient;

    public TriageResult triageTicket(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(String.format("Ticket not found with id: %s", ticketId)));

        String text = ticket.getSubject() + " " + ticket.getBody();

        List<KnowledgeDocument> documents = knowledgeBaseService.searchRelevantPolicies(text, 3);

        TriageContext context = TriageContext.builder()
                .ticketId(ticket.getId())
                .subject(ticket.getSubject())
                .body(ticket.getBody())
                .policies(documents)
                .build();

        TriageResult result = aiClient.triage(context);

        ticket.setTicketCategory(result.getCategory());
        ticket.setTicketPriority(result.getPriority());
        ticket.setTicketSentiment(result.getSentiment());
        ticket.setEscalationRequired(result.getEscalationRequired());
        ticket.setTriagedAt(OffsetDateTime.now());

        ticketRepository.save(ticket);
        return result;
    }
}
