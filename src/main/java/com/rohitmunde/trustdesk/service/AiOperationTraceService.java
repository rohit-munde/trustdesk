package com.rohitmunde.trustdesk.service;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.dto.AiOperationTraceDto;
import com.rohitmunde.trustdesk.dto.TriageResult;
import com.rohitmunde.trustdesk.entity.AiOperationTrace;
import com.rohitmunde.trustdesk.enums.AiOperationStatus;
import com.rohitmunde.trustdesk.repository.AiOperationTraceRepository;
import com.rohitmunde.trustdesk.service.interfaces.IAiOperationTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiOperationTraceService implements IAiOperationTraceService {
    private static final String TRIAGE_OPERATION = "TICKET_TRIAGE";

    private final ObjectMapper objectMapper;
    private final AiOperationTraceRepository aiOperationTraceRepository;

    @Transactional(readOnly = true)
    public List<AiOperationTraceDto> getTicketTraces(String ticketId) {
        return aiOperationTraceRepository.findByTicketIdOrderByCreatedAtDesc(ticketId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordSuccess(String provider, TriageContext context, TriageResult result) {
        AiOperationTrace aiOperationTrace = createAiOperationTrace(provider, context);
        // Set success-specific fields if needed
        aiOperationTrace.setStatus(AiOperationStatus.SUCCESS);
        aiOperationTrace.setOutputJson(objectMapper.writeValueAsString(result));

        // Save the trace (assuming you have a repository or similar)
        aiOperationTraceRepository.save(aiOperationTrace);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(String provider, TriageContext context, Exception exception) {
        AiOperationTrace aiOperationTrace = createAiOperationTrace(provider, context);
        // Set failure-specific fields if needed
        aiOperationTrace.setStatus(AiOperationStatus.FAILED);
        aiOperationTrace.setErrorMessage(exception.getMessage());

        // Save the trace (assuming you have a repository or similar)
        aiOperationTraceRepository.save(aiOperationTrace);
    }

    private AiOperationTrace createAiOperationTrace(String provider, TriageContext context) {
        AiOperationTrace aiOperationTrace = new AiOperationTrace();
        aiOperationTrace.setProvider(provider);
        aiOperationTrace.setTicketId(context.getTicketId());
        aiOperationTrace.setOperationType(TRIAGE_OPERATION);
        aiOperationTrace.setInputSummary(buildSummary(context));
        aiOperationTrace.setRetrievedPolicies(context.getPolicies().toString());
        aiOperationTrace.setCreatedAt(OffsetDateTime.now());

        return aiOperationTrace;
    }

    private AiOperationTraceDto toDto(AiOperationTrace trace) {
        return AiOperationTraceDto.builder()
                .id(trace.getId())
                .ticketId(trace.getTicketId())
                .operationType(trace.getOperationType())
                .provider(trace.getProvider())
                .status(trace.getStatus())
                .inputSummary(trace.getInputSummary())
                .retrievedPolicies(trace.getRetrievedPolicies())
                .outputJson(trace.getOutputJson())
                .errorMessage(trace.getErrorMessage())
                .createdAt(trace.getCreatedAt())
                .build();
    }

    private String buildSummary(TriageContext context) {
        return "Subject:" + context.getSubject() + " " + "\n Body: " + context.getBody();
    }
}
