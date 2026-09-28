import { of } from 'rxjs';
import { vi } from 'vitest';
import { TicketCategory } from '../../enum/TicketCategory.enum';
import { TicketChannel } from '../../enum/TicketChannel.enum';
import { TicketPriority } from '../../enum/TicketPriority.enum';
import { TicketSentiment } from '../../enum/TicketSentiment.enum';
import { TicketStatus } from '../../enum/TicketStatus.enum';
import { ITicketPayload, ITriagePayload } from '../../interface/response';
import { TicketService } from '../service/ticket-service';
import { TicketsComponent } from './tickets.component';

describe('TicketsComponent', () => {
  const ticket: ITicketPayload = {
    actionApprovalStatus: 'PENDING_APPROVAL',
    actionExecutedAt: null,
    actionExecutionReference: null,
    actionExecutionStatus: 'NOT_STARTED',
    category: TicketCategory.GENERAL,
    channel: TicketChannel.EMAIL,
    createdAt: '2026-06-28T10:15:00+05:30',
    customerId: 'cus_1001',
    body: 'Need help',
    escalationRequired: false,
    orderId: 'ord_5001',
    priority: TicketPriority.LOW,
    recommendedAction: 'CREATE_REPLACEMENT_ORDER',
    sentiment: TicketSentiment.NEUTRAL,
    status: TicketStatus.OPEN,
    subject: 'Support request',
    ticketId: 'tkt_9001',
    triagedAt: null,
  };

  let ticketService: TicketService;
  let component: TicketsComponent;

  beforeEach(() => {
    ticketService = {
      getAllTickets: vi.fn().mockReturnValue(of({ success: true, message: '', payload: [ticket] })),
      getTicketById: vi.fn().mockReturnValue(of({ success: true, message: '', payload: ticket })),
      updateTicketStatus: vi.fn().mockReturnValue(
        of({ success: true, message: '', payload: { ...ticket, status: TicketStatus.IN_PROGRESS } })
      ),
      updateTicketPriority: vi.fn().mockReturnValue(
        of({ success: true, message: '', payload: { ...ticket, priority: TicketPriority.URGENT } })
      ),
      triageTicket: vi.fn().mockReturnValue(
        of({
          success: true,
          message: '',
          payload: {
            category: TicketCategory.BILLING,
            priority: TicketPriority.HIGH,
            sentiment: TicketSentiment.FRUSTRATED,
            escalationRequired: true,
            citations: ['billing_policy.md'],
            draftReply: 'We are checking the billing issue.',
            recommendedAction: 'REVIEW_BILLING_CHARGE',
          },
        })
      ),
      approveTicketAction: vi.fn().mockReturnValue(
        of({ success: true, message: '', payload: { ...ticket, actionApprovalStatus: 'APPROVED' } })
      ),
      rejectTicketAction: vi.fn().mockReturnValue(
        of({ success: true, message: '', payload: { ...ticket, actionApprovalStatus: 'REJECTED' } })
      ),
      executeTicketAction: vi.fn().mockReturnValue(
        of({
          success: true,
          message: '',
          payload: {
            ...ticket,
            actionApprovalStatus: 'APPROVED',
            actionExecutionStatus: 'EXECUTED',
            actionExecutionReference: 'replacement_order:tkt_9001',
            actionExecutedAt: '2026-06-28T11:15:00+05:30',
          },
        })
      ),
    } as unknown as TicketService;

    component = new TicketsComponent(ticketService);
  });

  it('loads tickets and fetches the selected ticket details', () => {
    component.ngOnInit();

    expect(component.selectedId()).toBe('tkt_9001');
    expect(ticketService.getTicketById).toHaveBeenCalledWith('tkt_9001');
  });

  it('updates ticket status from the API response', () => {
    component.tickets.set([ticket]);

    component.updateStatus('tkt_9001', TicketStatus.IN_PROGRESS);

    expect(ticketService.updateTicketStatus).toHaveBeenCalledWith('tkt_9001', TicketStatus.IN_PROGRESS);
    expect(component.tickets()[0].status).toBe(TicketStatus.IN_PROGRESS);
  });

  it('updates ticket priority from the API response', () => {
    component.tickets.set([ticket]);

    component.updatePriority('tkt_9001', TicketPriority.URGENT);

    expect(ticketService.updateTicketPriority).toHaveBeenCalledWith('tkt_9001', TicketPriority.URGENT);
    expect(component.tickets()[0].priority).toBe(TicketPriority.URGENT);
  });

  it('stores triage output and merges ticket triage fields', () => {
    component.tickets.set([ticket]);
    component.selectedId.set('tkt_9001');

    component.runTriage('tkt_9001');

    const triageResult: ITriagePayload | null = component.selectedTriageResult();
    expect(ticketService.triageTicket).toHaveBeenCalledWith('tkt_9001');
    expect(triageResult?.draftReply).toBe('We are checking the billing issue.');
    expect(component.tickets()[0]).toEqual(expect.objectContaining({
      category: TicketCategory.BILLING,
      priority: TicketPriority.HIGH,
      sentiment: TicketSentiment.FRUSTRATED,
      escalationRequired: true,
      recommendedAction: 'REVIEW_BILLING_CHARGE',
    }));
  });

  it('approves a recommended action from the API response', () => {
    component.tickets.set([ticket]);

    component.approveAction('tkt_9001');

    expect(ticketService.approveTicketAction).toHaveBeenCalledWith('tkt_9001');
    expect(component.tickets()[0].actionApprovalStatus).toBe('APPROVED');
  });

  it('rejects a recommended action from the API response', () => {
    component.tickets.set([ticket]);

    component.rejectAction('tkt_9001');

    expect(ticketService.rejectTicketAction).toHaveBeenCalledWith('tkt_9001');
    expect(component.tickets()[0].actionApprovalStatus).toBe('REJECTED');
  });

  it('executes an approved action from the API response', () => {
    component.tickets.set([{ ...ticket, actionApprovalStatus: 'APPROVED' }]);

    component.executeAction('tkt_9001');

    expect(ticketService.executeTicketAction).toHaveBeenCalledWith('tkt_9001');
    expect(component.tickets()[0]).toEqual(expect.objectContaining({
      actionExecutionStatus: 'EXECUTED',
      actionExecutionReference: 'replacement_order:tkt_9001',
    }));
  });
});
