import { TicketCategory } from "../enum/TicketCategory.enum";
import { TicketChannel } from "../enum/TicketChannel.enum";
import { TicketPriority } from "../enum/TicketPriority.enum";
import { TicketSentiment } from "../enum/TicketSentiment.enum";
import { TicketStatus } from "../enum/TicketStatus.enum";

export type RecommendedAction =
    'NO_ACTION'
    | 'CREATE_REPLACEMENT_ORDER'
    | 'START_REFUND_REVIEW'
    | 'CHECK_SHIPPING_STATUS'
    | 'REVIEW_BILLING_CHARGE'
    | 'REVIEW_WARRANTY_CLAIM'
    | 'REVIEW_ACCOUNT_SECURITY';

export type ActionApprovalStatus = 'NOT_REQUIRED' | 'PENDING_APPROVAL' | 'APPROVED' | 'REJECTED';
export type ActionExecutionStatus = 'NOT_STARTED' | 'EXECUTED' | 'FAILED';

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
    actionApprovalStatus?: ActionApprovalStatus | null;
    actionExecutedAt?: string | null;
    actionExecutionReference?: string | null;
    actionExecutionStatus?: ActionExecutionStatus | null;
    category: TicketCategory;
    channel: TicketChannel;
    createdAt: string;
    customerId: string;
    body: string;
    escalationRequired: boolean | null;
    orderId: string;
    priority: TicketPriority;
    recommendedAction?: RecommendedAction | null;
    sentiment: TicketSentiment;
    status: TicketStatus;
    subject: string;
    ticketId: string;
    triagedAt: string | null;
    draftReply?: string | null;
    citations?: string[] | null;
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
    recommendedAction: RecommendedAction;
}

export interface ITriageResponse extends IApiSuccessResponse<ITriagePayload> { }
