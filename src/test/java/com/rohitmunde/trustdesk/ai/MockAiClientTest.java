package com.rohitmunde.trustdesk.ai;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.enums.RecommendedAction;
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
        Assertions.assertThat(result.getRecommendedAction()).isEqualTo(RecommendedAction.CREATE_REPLACEMENT_ORDER);
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
        Assertions.assertThat(result.getRecommendedAction()).isEqualTo(RecommendedAction.NO_ACTION);
    }

    @Test
    void triagesTrackingTicketAsShipping() {
        var result = mockAiClient.triage(TriageContext.builder()
                .subject("Where is my order?")
                .body("The tracking link has not updated for two days. Can you check the order status?")
                .policies(java.util.List.of(policy("shipping_policy.md")))
                .build());

        Assertions.assertThat(result.getCategory()).isEqualTo(TicketCategory.SHIPPING);
        Assertions.assertThat(result.getPriority()).isEqualTo(TicketPriority.LOW);
        Assertions.assertThat(result.getSentiment()).isEqualTo(TicketSentiment.NEUTRAL);
        Assertions.assertThat(result.getRecommendedAction()).isEqualTo(RecommendedAction.CHECK_SHIPPING_STATUS);
    }

    private KnowledgeDocument policy(String sourceFile) {
        return new KnowledgeDocument("policy", "Policy", "Policy content", sourceFile);
    }
}
