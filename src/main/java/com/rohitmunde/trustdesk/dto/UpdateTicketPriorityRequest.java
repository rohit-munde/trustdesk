package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.enums.TicketPriority;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTicketPriorityRequest {

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}
