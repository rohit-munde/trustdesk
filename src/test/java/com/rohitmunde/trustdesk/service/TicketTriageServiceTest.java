package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.TicketRepository;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.entity.Ticket;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.model.KnowledgeDocument;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketTriageServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private KnowledgeBaseService knowledgeBaseService;

    @InjectMocks
    private TicketTriageService ticketTriageService;

    @Test
    void shouldTriageAndUpdateTicket() {
        Ticket ticket = new Ticket();

        ticket.setId("tkt_9002");
        ticket.setSubject("Unable to access my account");
        ticket.setBody("I have been trying to log in to my account but keep getting an error message saying 'Invalid credentials'. I have reset my password but it still doesn't work. Please help me regain access to my account as soon as possible. Thank you.");

        KnowledgeDocument policy = new KnowledgeDocument( "replacement-policy",
                "Replacement Policy",
                "Damaged products may be replaced.",
                "replacement-policy.md");

        TriageResult triageResult = TriageResult.builder()
                .category(TicketCategory.REFUND)
                .priority(TicketPriority.MEDIUM)
                .sentiment(TicketSentiment.FRUSTRATED)
                .escalationRequired(false)
                .citations(List.of(policy.getSourceFile()))
                .build();

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        when(knowledgeBaseService.searchRelevantPolicies(ticket.getSubject() + " " + ticket.getBody(), 3))
                .thenReturn(List.of(policy));

        when(ticketTriageService.triageTicket(ticket.getId())).thenReurn(triageResult);

        TriageResult triagedResult = ticketTriageService.triageTicket(ticket.getId());

        Assertions.assertThat(triagedResult).isEqualTo(triageResult);
        Assertions.assertThat(ticket.getTicketCategory()).isEqualTo(TicketCategory.REFUND);
        Assertions.assertThat(ticket.getTicketPriority()).isEqualTo(TicketPriority.MEDIUM);
        Assertions.assertThat(ticket.getTicketSentiment()).isEqualTo(TicketSentiment.FRUSTRATED);
        Assertions.assertThat(ticket.getEscalationRequired()).isFalse();
    }

}
