package com.rohitmunde.trustdesk.controller;

import com.rohitmunde.trustdesk.dto.TriageEvaluationSummaryDto;
import com.rohitmunde.trustdesk.response.ApiSuccessResponse;
import com.rohitmunde.trustdesk.service.TriageEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/evaluations")
public class EvaluationController {

    private final TriageEvaluationService triageEvaluationService;

    @PostMapping("/triage")
    public ApiSuccessResponse<TriageEvaluationSummaryDto> runTriageEvaluation() {
        return new ApiSuccessResponse<>(
                "Triage evaluation completed successfully",
                triageEvaluationService.runSeededEvaluation()
        );
    }
}
