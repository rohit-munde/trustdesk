package com.rohitmunde.trustdesk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KnowledgeDocument {
    private String id;
    private String title;
    private String content;
    private String sourceFile;
}
