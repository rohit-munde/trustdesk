package com.rohitmunde.trustdesk.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TriageEvaluationSummaryDto {
    private int totalCases;
    private int passedCases;
    private int failedCases;
    private List<TriageEvaluationCaseResultDto> results;
}
