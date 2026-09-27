import { TicketPriority } from "../enum/TicketPriority.enum";
import { TicketStatus } from "../enum/TicketStatus.enum";

export interface IUpdateTicketStatusRequest {
    status: TicketStatus;
}

export interface IUpdateTicketPriorityRequest {
    priority: TicketPriority;
}
