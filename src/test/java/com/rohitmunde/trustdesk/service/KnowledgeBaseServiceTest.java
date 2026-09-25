package com.rohitmunde.trustdesk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

@SpringBootTest
public class KnowledgeBaseServiceTest {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Test
    void shouldLoadMarkDownFiles() {
        var documents = knowledgeBaseService.getAllDocuments();

        Assertions.assertThat(documents).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getId()).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getTitle()).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getContent()).isNotEmpty();
        Assertions.assertThat(documents.getFirst().getSourceFile()).isNotEmpty();
    }
}
