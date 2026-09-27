export type TicketStatus = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
export type TicketPriority = 'HIGH' | 'MEDIUM' | 'LOW';
export type TicketCategory = 'SHIPPING' | 'REFUND' | 'WARRANTY' | 'BILLING' | 'ACCOUNT_SECURITY' | 'GENERAL';
export type TicketSentiment = 'FRUSTRATED' | 'NEUTRAL' | 'POSITIVE';

export interface ITicket {
  id: string;
  subject: string;
  body: string;
  createdAt: string;
  status: TicketStatus;
  priority: TicketPriority;
  category: TicketCategory;
  sentiment: TicketSentiment;
  customerName: string;
  customerEmail: string;
  escalationRequired: boolean;
  draftReply: string;
  citations: string[];
}
