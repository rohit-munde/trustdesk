package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.model.KnowledgeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TriageContext {

    private String ticketId;
    private String subject;
    private String body;
    private List<KnowledgeDocument> policies;
}
