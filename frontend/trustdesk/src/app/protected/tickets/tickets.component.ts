import { Component, computed, OnInit, signal } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { TicketService } from '../service/ticket-service';
import { ActionApprovalStatus, ActionExecutionStatus, ITicketPayload, ITicketResponse, ITriagePayload, RecommendedAction } from '../../interface/response';
import { TicketPriority } from '../../enum/TicketPriority.enum';
import { TicketStatus } from '../../enum/TicketStatus.enum';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [NgClass, DatePipe],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.scss',
})
export class TicketsComponent implements OnInit {
  readonly tickets = signal<ITicketPayload[]>([]);
  readonly selectedId = signal<string | null>(null);
  readonly triageResults = signal<Record<string, ITriagePayload>>({});
  readonly actionInFlight = signal<string | null>(null);
  readonly statusOptions = Object.values(TicketStatus);
  readonly priorityOptions = Object.values(TicketPriority);

  readonly selectedTicket = computed(() =>
    this.tickets().find((ticket) => ticket.ticketId === this.selectedId()) ?? null
  );

  readonly selectedTriageResult = computed(() => {
    const ticketId = this.selectedId();

    return ticketId ? this.triageResults()[ticketId] ?? null : null;
  });

  constructor(private ticketService: TicketService) { }

  ngOnInit(): void {
    this.ticketService.getAllTickets().subscribe((response: ITicketResponse) => {
      this.tickets.set(response.payload);

      if (response.payload.length > 0 && this.selectedId() === null) {
        this.select(response.payload[0]);
      }
    });
  }

  select(ticket: ITicketPayload): void {
    this.selectedId.set(ticket.ticketId);
    this.ticketService.getTicketById(ticket.ticketId).subscribe((response) => {
      this.replaceTicket(response.payload);
    });
  }

  updateStatus(ticketId: string, status: TicketStatus | string): void {
    const nextStatus = status as TicketStatus;
    this.actionInFlight.set(`status:${ticketId}`);

    this.ticketService.updateTicketStatus(ticketId, nextStatus).subscribe({
      next: (response) => this.replaceTicket(response.payload),
      error: () => this.actionInFlight.set(null),
      complete: () => this.actionInFlight.set(null),
    });
  }

  updatePriority(ticketId: string, priority: TicketPriority | string): void {
    const nextPriority = priority as TicketPriority;
    this.actionInFlight.set(`priority:${ticketId}`);

    this.ticketService.updateTicketPriority(ticketId, nextPriority).subscribe({
      next: (response) => this.replaceTicket(response.payload),
      error: () => this.actionInFlight.set(null),
      complete: () => this.actionInFlight.set(null),
    });
  }

  runTriage(ticketId: string): void {
    this.actionInFlight.set(`triage:${ticketId}`);

    this.ticketService.triageTicket(ticketId).subscribe({
      next: (response) => {
        const triageResult = response.payload;
        this.triageResults.update((results) => ({ ...results, [ticketId]: triageResult }));
        this.mergeTicket(ticketId, {
          category: triageResult.category,
          priority: triageResult.priority,
          sentiment: triageResult.sentiment,
          escalationRequired: triageResult.escalationRequired,
          recommendedAction: triageResult.recommendedAction,
        });
      },
      error: () => this.actionInFlight.set(null),
      complete: () => this.actionInFlight.set(null),
    });
  }

  approveAction(ticketId: string): void {
    this.actionInFlight.set(`approve:${ticketId}`);

    this.ticketService.approveTicketAction(ticketId).subscribe({
      next: (response) => this.replaceTicket(response.payload),
      error: () => this.actionInFlight.set(null),
      complete: () => this.actionInFlight.set(null),
    });
  }

  rejectAction(ticketId: string): void {
    this.actionInFlight.set(`reject:${ticketId}`);

    this.ticketService.rejectTicketAction(ticketId).subscribe({
      next: (response) => this.replaceTicket(response.payload),
      error: () => this.actionInFlight.set(null),
      complete: () => this.actionInFlight.set(null),
    });
  }

  executeAction(ticketId: string): void {
    this.actionInFlight.set(`execute:${ticketId}`);

    this.ticketService.executeTicketAction(ticketId).subscribe({
      next: (response) => this.replaceTicket(response.payload),
      error: () => this.actionInFlight.set(null),
      complete: () => this.actionInFlight.set(null),
    });
  }

  isActionLoading(action: 'status' | 'priority' | 'triage' | 'approve' | 'reject' | 'execute', ticketId: string): boolean {
    return this.actionInFlight() === `${action}:${ticketId}`;
  }

  priorityDotClass(priority: TicketPriority): string {
    return { URGENT: 'urgent', HIGH: 'urgent', MEDIUM: 'warning', LOW: 'neutral' }[priority];
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

  actionLabel(action: RecommendedAction | null | undefined): string {
    if (!action || action === 'NO_ACTION') {
      return 'No action';
    }

    return this.categoryLabel(action);
  }

  approvalLabel(status: ActionApprovalStatus | null | undefined): string {
    return this.categoryLabel(status ?? 'PENDING_APPROVAL');
  }

  executionLabel(status: ActionExecutionStatus | null | undefined): string {
    return this.categoryLabel(status ?? 'NOT_STARTED');
  }

  canApproveAction(ticket: ITicketPayload): boolean {
    return ticket.recommendedAction !== undefined
      && ticket.recommendedAction !== null
      && ticket.recommendedAction !== 'NO_ACTION'
      && ticket.actionApprovalStatus === 'PENDING_APPROVAL';
  }

  canExecuteAction(ticket: ITicketPayload): boolean {
    return ticket.recommendedAction !== undefined
      && ticket.recommendedAction !== null
      && ticket.recommendedAction !== 'NO_ACTION'
      && ticket.actionApprovalStatus === 'APPROVED'
      && ticket.actionExecutionStatus !== 'EXECUTED';
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

  private replaceTicket(updatedTicket: ITicketPayload): void {
    this.tickets.update((tickets) =>
      tickets.map((ticket) => ticket.ticketId === updatedTicket.ticketId ? updatedTicket : ticket)
    );
  }

  private mergeTicket(ticketId: string, updates: Partial<ITicketPayload>): void {
    this.tickets.update((tickets) =>
      tickets.map((ticket) => ticket.ticketId === ticketId ? { ...ticket, ...updates } : ticket)
    );
  }
}
