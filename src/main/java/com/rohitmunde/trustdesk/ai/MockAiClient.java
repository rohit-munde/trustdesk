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
public class MockAiClient implements AiClient {

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
                    .draftReply(buildDraftReply(TicketCategory.REFUND))
                    .build();
        }

        return TriageResult.builder()
                .category(TicketCategory.GENERAL)
                .priority(TicketPriority.LOW)
                .sentiment(TicketSentiment.NEUTRAL)
                .escalationRequired(false)
                .citations(citations)
                .draftReply(buildDraftReply(TicketCategory.GENERAL))
                .build();
    }

    private String buildDraftReply(TicketCategory category) {
        return switch (category) {
            case REFUND -> "I'm sorry your item arrived damaged. Based on our refund and replacement policy, we can help review this for a refund or replacement.";
            case BILLING -> "Thanks for reaching out. We'll review the billing details and help resolve any incorrect charge.";
            case SHIPPING -> "Thanks for contacting us. We'll check the shipping status and tracking details for your order.";
            case WARRANTY -> "Thanks for reaching out. We'll review the product warranty details and help confirm the next available support option.";
            case ACCOUNT_SECURITY -> "Thanks for reporting this. We'll review the account security concern and help protect your account.";
            case GENERAL -> "Thank you for your patience. We're looking into this for you.";
        };
    }
}
