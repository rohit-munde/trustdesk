package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.model.KnowledgeDocument;
import com.rohitmunde.trustdesk.service.interfaces.IKnowledgeBaseService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KnowledgeBaseService implements IKnowledgeBaseService {

    List<KnowledgeDocument> documents = new ArrayList<>();

    @Override
    @PostConstruct //contruct the knowledge docs into the memory when the service is created
    public void LoadAllDocuments() {
        try{
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:knowledge-base/**/*.md");
            log.info("Found {} markdown files", resources.length);
            for (Resource resource : resources) {
               documents.add(createDocument(resource));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<KnowledgeDocument> getAllDocuments() {
        return documents;
    }

    private KnowledgeDocument createDocument(Resource resource)
            throws IOException {

        String sourceFile = resource.getFilename();

        try (InputStream inputStream = resource.getInputStream()) {
            String content = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String id = removeMarkdownExtension(sourceFile);
            String title = extractTitle(content, id);

            return new KnowledgeDocument(
                    id,
                    title,
                    content,
                    sourceFile
            );
        }
    }

    private String extractTitle(String content, String fallbackTitle) {
        return content.lines()
                .filter(line -> line.startsWith("# "))
                .map(line -> line.substring(2).trim())
                .findFirst()
                .orElse(fallbackTitle);
    }

    private String removeMarkdownExtension(String filename) {
        if (filename == null) {
            return "unknown";
        }

        return filename.replaceFirst("\\.md$", "");
    }
}
