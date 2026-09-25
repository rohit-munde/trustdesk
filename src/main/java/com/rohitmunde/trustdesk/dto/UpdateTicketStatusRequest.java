package com.rohitmunde.trustdesk.dto;

import com.rohitmunde.trustdesk.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTicketStatusRequest {

    @NotNull(message = "Status is required")
    TicketStatus status;
}
