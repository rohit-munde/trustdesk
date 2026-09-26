import { Component, computed, signal } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { ITicket, TicketPriority, TicketStatus } from '../../interface/ticket';

const MOCK_TICKETS: ITicket[] = [
  {
    id: 'TKT-001',
    subject: 'Delayed delivery',
    body: "Where is my order? It's been over two weeks since I placed the order and I still haven't received anything. The tracking page shows no updates.",
    createdAt: '2024-04-24T09:12:00Z',
    status: 'OPEN',
    priority: 'HIGH',
    category: 'SHIPPING',
    sentiment: 'FRUSTRATED',
    customerName: 'Emily Carter',
    customerEmail: 'emily.carter@email.com',
    escalationRequired: true,
    draftReply:
      "Hi Emily, we're so sorry to hear your order hasn't arrived yet. We've escalated this to our fulfilment team and will have an update for you within 24 hours. We sincerely apologise for the inconvenience.",
    citations: ['shipping_policy.md', 'escalation_guidelines.md'],
  },
  {
    id: 'TKT-002',
    subject: 'Refund request',
    body: 'My headphones arrived damaged — the left earbud is cracked and the case hinge is broken. I would like a full refund or a replacement sent immediately.',
    createdAt: '2024-04-23T14:45:00Z',
    status: 'IN_PROGRESS',
    priority: 'MEDIUM',
    category: 'REFUND',
    sentiment: 'FRUSTRATED',
    customerName: 'Marcus Webb',
    customerEmail: 'marcus.webb@email.com',
    escalationRequired: false,
    draftReply:
      "Hi Marcus, I'm sorry your item arrived damaged. Based on our refund and replacement policy, we can process a full refund or send a replacement — whichever you prefer. Please let us know and we'll take care of it right away.",
    citations: ['refund_policy.md'],
  },
  {
    id: 'TKT-003',
    subject: 'Account access',
    body: "I can't log in to my account. I've tried resetting my password multiple times but the reset email never arrives.",
    createdAt: '2024-04-22T11:00:00Z',
    status: 'OPEN',
    priority: 'LOW',
    category: 'ACCOUNT_SECURITY',
    sentiment: 'NEUTRAL',
    customerName: 'Priya Nair',
    customerEmail: 'priya.nair@email.com',
    escalationRequired: false,
    draftReply:
      "Hi Priya, thank you for reaching out. Please check your spam folder for the reset email. If it's still missing, our team can manually reset your account — just confirm your registered email and we'll handle it.",
    citations: ['account_security_policy.md'],
  },
  {
    id: 'TKT-004',
    subject: 'Incorrect billing charge',
    body: "I was charged twice for the same order last Tuesday. My card statement shows two identical transactions of $89.99. Please refund the duplicate charge.",
    createdAt: '2024-04-21T16:20:00Z',
    status: 'OPEN',
    priority: 'HIGH',
    category: 'BILLING',
    sentiment: 'FRUSTRATED',
    customerName: 'James Liu',
    customerEmail: 'james.liu@email.com',
    escalationRequired: true,
    draftReply:
      "Hi James, we sincerely apologise for the duplicate charge. Our billing team has been notified and the extra $89.99 will be refunded to your card within 3–5 business days.",
    citations: ['billing_policy.md', 'refund_policy.md'],
  },
  {
    id: 'TKT-005',
    subject: 'Warranty claim for smartwatch',
    body: 'My smartwatch screen cracked after only 3 months of normal use. I have not dropped it. I believe this is a manufacturing defect and should be covered under warranty.',
    createdAt: '2024-04-20T08:30:00Z',
    status: 'RESOLVED',
    priority: 'MEDIUM',
    category: 'WARRANTY',
    sentiment: 'NEUTRAL',
    customerName: 'Sofia Reyes',
    customerEmail: 'sofia.reyes@email.com',
    escalationRequired: false,
    draftReply:
      "Hi Sofia, thank you for contacting us. A cracked screen from normal use within the first year is covered under our warranty. We'll send you a prepaid shipping label to return the watch, and a replacement will be dispatched within 5 business days.",
    citations: ['warranty_policy.md'],
  },
  {
    id: 'TKT-006',
    subject: 'Question about return window',
    body: 'I purchased a jacket 28 days ago. Your website says 30-day returns. Am I still eligible to return it? I just do not like the colour in natural light.',
    createdAt: '2024-04-19T13:05:00Z',
    status: 'CLOSED',
    priority: 'LOW',
    category: 'GENERAL',
    sentiment: 'NEUTRAL',
    customerName: 'Tom Bradley',
    customerEmail: 'tom.bradley@email.com',
    escalationRequired: false,
    draftReply:
      "Hi Tom, great news — you're within the 30-day return window! You can initiate a return through your account portal or reply here and we'll send you a return label.",
    citations: ['refund_policy.md'],
  },
];

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [NgClass, DatePipe],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.scss',
})
export class TicketsComponent {
  readonly tickets = signal<ITicket[]>(MOCK_TICKETS);
  readonly selectedId = signal<string | null>(MOCK_TICKETS[1].id);

  readonly selectedTicket = computed(() =>
    this.tickets().find((t) => t.id === this.selectedId()) ?? null
  );

  select(ticket: ITicket): void {
    this.selectedId.set(ticket.id);
  }

  priorityDotClass(priority: TicketPriority): string {
    return { HIGH: 'urgent', MEDIUM: 'warning', LOW: 'neutral' }[priority];
  }

  statusLabel(status: TicketStatus): string {
    return { OPEN: 'Open', IN_PROGRESS: 'In Progress', RESOLVED: 'Resolved', CLOSED: 'Closed' }[status];
  }

  statusClass(status: TicketStatus): string {
    return {
      OPEN: 'badge-open',
      IN_PROGRESS: 'badge-progress',
      RESOLVED: 'badge-resolved',
      CLOSED: 'badge-closed',
    }[status];
  }

  categoryLabel(category: string): string {
    return category.replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
  }

  sentimentIcon(sentiment: string): string {
    return { FRUSTRATED: 'sentiment_dissatisfied', NEUTRAL: 'sentiment_neutral', POSITIVE: 'sentiment_satisfied' }[sentiment] ?? 'sentiment_neutral';
  }

  sentimentClass(sentiment: string): string {
    return { FRUSTRATED: 'sentiment-frustrated', NEUTRAL: 'sentiment-neutral', POSITIVE: 'sentiment-positive' }[sentiment] ?? '';
  }

  copiedReply = false;

  copyReply(text: string): void {
    navigator.clipboard.writeText(text);
    this.copiedReply = true;
    setTimeout(() => (this.copiedReply = false), 1500);
  }
}
