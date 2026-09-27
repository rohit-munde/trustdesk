package com.rohitmunde.trustdesk.service;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class KnowledgeBaseServiceTest {

    @Test
    void shouldLoadMarkDownFiles() {
        KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();
        knowledgeBaseService.LoadAllDocuments();

        var documents = knowledgeBaseService.getAllDocuments();

        Assertions.assertThat(documents).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getId()).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getTitle()).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getContent()).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getSourceFile()).isNotEmpty();
    }

    @Test
    void shouldReturnRelevantPoliciesForTicketText() {
        KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();
        knowledgeBaseService.LoadAllDocuments();

        var documents = knowledgeBaseService.searchRelevantPolicies("damaged cracked earbuds replacement", 3);

        Assertions.assertThat(documents)
                .extracting("sourceFile")
                .contains("warranty_policy.md");
    }
}
