package com.rohitmunde.trustdesk.service.interfaces;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.dto.TriageResult;

public interface IAiOperationTraceService {

    void recordSuccess(String provider, TriageContext context, TriageResult result);

    void recordFailure(String provider, TriageContext context, Exception exception);
}
