package com.rohitmunde.trustdesk.ai;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.model.KnowledgeDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MockAiClientTest {

    private final MockAiClient mockAiClient = new MockAiClient();

    @Test
    void triagesDamagedTicketAsRefund() {
        var result = mockAiClient.triage(TriageContext.builder()
                .subject("Received damaged earbuds")
                .body("The left earbud arrived cracked.")
                .policies(java.util.List.of(policy("refund_policy.md")))
                .build());

        Assertions.assertThat(result.getCategory()).isEqualTo(TicketCategory.REFUND);
        Assertions.assertThat(result.getPriority()).isEqualTo(TicketPriority.MEDIUM);
        Assertions.assertThat(result.getSentiment()).isEqualTo(TicketSentiment.FRUSTRATED);
        Assertions.assertThat(result.getCitations()).containsExactly("refund_policy.md");
        Assertions.assertThat(result.getDraftReply()).isEqualTo("I'm sorry your item arrived damaged. Based on our refund and replacement policy, we can help review this for a refund or replacement.");
    }

    @Test
    void triagesGeneralTicketAsLowPriority() {
        var result = mockAiClient.triage(TriageContext.builder()
                .subject("Question about account")
                .body("I want to know where to update my phone number.")
                .policies(java.util.List.of(policy("account_security_policy.md")))
                .build());

        Assertions.assertThat(result.getCategory()).isEqualTo(TicketCategory.GENERAL);
        Assertions.assertThat(result.getPriority()).isEqualTo(TicketPriority.LOW);
        Assertions.assertThat(result.getSentiment()).isEqualTo(TicketSentiment.NEUTRAL);
        Assertions.assertThat(result.getCitations()).containsExactly("account_security_policy.md");
        Assertions.assertThat(result.getDraftReply()).isEqualTo("Thank you for your patience. We're looking into this for you.");
    }

    private KnowledgeDocument policy(String sourceFile) {
        return new KnowledgeDocument("policy", "Policy", "Policy content", sourceFile);
    }
}
