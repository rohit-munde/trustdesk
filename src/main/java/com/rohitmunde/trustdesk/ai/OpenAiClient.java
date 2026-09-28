package com.rohitmunde.trustdesk.ai;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.enums.TicketCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;

import com.rohitmunde.trustdesk.enums.RecommendedAction;
import com.rohitmunde.trustdesk.enums.TicketPriority;
import com.rohitmunde.trustdesk.enums.TicketSentiment;
import com.rohitmunde.trustdesk.model.KnowledgeDocument;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "trustdesk.ai.provider", havingValue = "openai")
public class OpenAiClient implements AiClient {
    // Implementation for OpenAI client goes here
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public OpenAiClient(
            ObjectMapper objectMapper,
            @Value("${trustdesk.ai.openai.api-key}") String apiKey,
            @Value("${trustdesk.ai.openai.model}") String model
            ) {
        this.objectMapper = objectMapper;
        this.model = model;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public TriageResult triage(TriageContext context) {
        String prompt = buildPrompt(context);

        Map<String, Object> request = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "You are TrustDesk, an AI support triage assistant. Return only valid JSON."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.2
        );

        Map<?, ?> response = restClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(Map.class);

        String content = extractContent(response);
        return parseResult(content, context);
    }

    private String buildPrompt(TriageContext context) {
        String policies = context.getPolicies().stream()
                .map(document -> "- Source: " + document.getSourceFile() + "\n" + document.getContent())
                .reduce("", (left, right) -> left + "\n" + right);

        return """
                Triage this customer support ticket.

                Ticket ID:
                %s

                Subject:
                %s

                Body:
                %s

                Relevant policies:
                %s

                Return JSON only with this exact shape:
                {
                  "category": "SHIPPING | REFUND | WARRANTY | BILLING | ACCOUNT_SECURITY | GENERAL",
                  "priority": "LOW | MEDIUM | HIGH | URGENT",
                  "sentiment": "FRUSTRATED | NEUTRAL | POSITIVE",
                  "escalationRequired": true,
                  "citations": ["source_file.md"],
                  "draftReply": "customer-facing reply",
                  "recommendedAction": "NO_ACTION | CREATE_REPLACEMENT_ORDER | START_REFUND_REVIEW | CHECK_SHIPPING_STATUS | REVIEW_BILLING_CHARGE | REVIEW_WARRANTY_CLAIM | REVIEW_ACCOUNT_SECURITY"
                }
                """.formatted(
                context.getTicketId(),
                context.getSubject(),
                context.getBody(),
                policies
        );
    }

    private String extractContent(Map<?, ?> response) {
        List<?> choices = (List<?>) response.get("choices");
        Map<?, ?> firstChoice = (Map<?, ?>) choices.getFirst();
        Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");
        return (String) message.get("content");
    }

    private TriageResult parseResult(String content, TriageContext context) {
        try {
            JsonNode root = objectMapper.readTree(content);

            return TriageResult.builder()
                    .category(enumValue(TicketCategory.class, root, "category", TicketCategory.GENERAL))
                    .priority(enumValue(TicketPriority.class, root, "priority", TicketPriority.LOW))
                    .sentiment(enumValue(TicketSentiment.class, root, "sentiment", TicketSentiment.NEUTRAL))
                    .escalationRequired(root.path("escalationRequired").asBoolean(false))
                    .citations(parseCitations(root, context))
                    .draftReply(root.path("draftReply").asText("Thank you for your patience. We're looking into this for you."))
                    .recommendedAction(enumValue(RecommendedAction.class, root, "recommendedAction", RecommendedAction.NO_ACTION))
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse AI triage response", ex);
        }
    }

    private List<String> parseCitations(JsonNode root, TriageContext context) {
        if (root.has("citations") && root.get("citations").isArray()) {
            return objectMapper.convertValue(
                    root.get("citations"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );
        }

        return context.getPolicies().stream()
                .map(KnowledgeDocument::getSourceFile)
                .toList();
    }

    private <T extends Enum<T>> T enumValue(Class<T> enumType, JsonNode root, String fieldName, T fallback) {
        String value = root.path(fieldName).asText(null);

        if (value == null || value.isBlank()) {
            return fallback;
        }

        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }
}
