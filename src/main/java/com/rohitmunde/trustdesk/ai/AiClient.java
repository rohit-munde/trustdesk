package com.rohitmunde.trustdesk.ai;

import com.rohitmunde.trustdesk.dto.TriageContext;
import com.rohitmunde.trustdesk.dto.TriageResult;

public interface AiClient {

    TriageResult triage(TriageContext context);
}
