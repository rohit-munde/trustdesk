import { TicketCategory } from "../enum/TicketCategory.enum";
import { TicketChannel } from "../enum/TicketChannel.enum";
import { TicketPriority } from "../enum/TicketPriority.enum";
import { TicketSentiment } from "../enum/TicketSentiment.enum";
import { TicketStatus } from "../enum/TicketStatus.enum";

export interface IApiSuccessResponse<T> {
    success: boolean,
    message: string,
    payload: T
}

export interface IApiErrorResponse {
    timestamp: string,
    status: number,
    error: string,
    message: string,
    path: string,
    validationErrors: Record<string, string>
}
export interface ITicketPayload {
    category: TicketCategory;
    channel: TicketChannel;
    createdAt: string;
    customerId: string;
    body: string;
    escalationRequired: boolean | null;
    orderId: string;
    priority: TicketPriority;
    sentiment: TicketSentiment;
    status: TicketStatus;
    subject: string;
    ticketId: string;
    triagedAt: string | null;
}

export interface ITicketResponse extends IApiSuccessResponse<ITicketPayload[]> { }
export interface ISingleTicketResponse extends IApiSuccessResponse<ITicketPayload> { }

export interface ITriagePayload {
    category: TicketCategory;
    priority: TicketPriority;
    sentiment: TicketSentiment;
    escalationRequired: boolean;
    citations: string[];
    draftReply: string;
}

export interface ITriageResponse extends IApiSuccessResponse<ITriagePayload> { }
