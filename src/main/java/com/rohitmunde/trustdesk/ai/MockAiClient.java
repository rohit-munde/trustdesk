package com.rohitmunde.trustdesk.ai;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.model.KnowledgeDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockAiClient implements AiClient{

    @Override
    public TriageResult triage(TriageContext context) {
        String text = context.getSubject() + " " + context.getBody();
        List<String> citations = context.getPolicies()
                .stream()
                .map(KnowledgeDocument::getSourceFile)
                .toList();

        if (text.contains("damaged") || text.contains("broken") || text.contains("defective") || text.contains("cracked")) {
            return TriageResult.builder()
                    .category(TicketCategory.REFUND)
                    .priority(TicketPriority.MEDIUM)
                    .sentiment(TicketSentiment.FRUSTRATED)
                    .escalationRequired(false)
                    .citations(citations)
                    .build();
        }

        return TriageResult.builder()
                .category(TicketCategory.GENERAL)
                .priority(TicketPriority.LOW)
                .sentiment(TicketSentiment.NEUTRAL)
                .escalationRequired(false)
                .citations(citations)
                .build();
    }
}
