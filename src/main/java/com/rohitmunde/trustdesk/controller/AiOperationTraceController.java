package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.dto.AiOperationTraceDto;
import com.rohitmunde.trustdesk.response.ApiSuccessResponse;
import com.rohitmunde.trustdesk.service.AiOperationTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets/{ticketId}/traces")
public class AiOperationTraceController {

    private final AiOperationTraceService aiOperationTraceService;

    @GetMapping
    public ApiSuccessResponse<List<AiOperationTraceDto>> getTicketTraces(@PathVariable String ticketId) {
        return new ApiSuccessResponse<>(
                "AI operation traces fetched successfully",
                aiOperationTraceService.getTicketTraces(ticketId)
        );
    }
}
