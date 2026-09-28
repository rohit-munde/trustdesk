import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TicketPriority } from '../../enum/TicketPriority.enum';
import { TicketStatus } from '../../enum/TicketStatus.enum';
import { TicketService } from './ticket-service';

describe('TicketService', () => {
  let service: TicketService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TicketService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('fetches all tickets', () => {
    service.getAllTickets().subscribe();

    const request = httpTesting.expectOne('/tickets');

    expect(request.request.method).toBe('GET');

    request.flush({ success: true, message: 'Tickets fetched successfully', payload: [] });
  });

  it('fetches one ticket by id', () => {
    service.getTicketById('tkt_9001').subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001');

    expect(request.request.method).toBe('GET');

    request.flush({ success: true, message: 'Ticket fetched successfully', payload: {} });
  });

  it('updates ticket status', () => {
    service.updateTicketStatus('tkt_9001', TicketStatus.IN_PROGRESS).subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001/status');

    expect(request.request.method).toBe('PATCH');
    expect(request.request.body).toEqual({ status: TicketStatus.IN_PROGRESS });

    request.flush({ success: true, message: 'Ticket updated successfully', payload: {} });
  });

  it('updates ticket priority', () => {
    service.updateTicketPriority('tkt_9001', TicketPriority.URGENT).subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001/priority');

    expect(request.request.method).toBe('PATCH');
    expect(request.request.body).toEqual({ priority: TicketPriority.URGENT });

    request.flush({ success: true, message: 'Ticket updated successfully', payload: {} });
  });

  it('triages a ticket', () => {
    service.triageTicket('tkt_9001').subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001/triage');

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBeNull();

    request.flush({ success: true, message: 'Ticket triaged successfully', payload: {} });
  });

  it('approves a ticket action', () => {
    service.approveTicketAction('tkt_9001').subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001/actions/approve');

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBeNull();

    request.flush({ success: true, message: 'Ticket updated successfully', payload: {} });
  });

  it('rejects a ticket action', () => {
    service.rejectTicketAction('tkt_9001').subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001/actions/reject');

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBeNull();

    request.flush({ success: true, message: 'Ticket updated successfully', payload: {} });
  });

  it('executes a ticket action', () => {
    service.executeTicketAction('tkt_9001').subscribe();

    const request = httpTesting.expectOne('/tickets/tkt_9001/actions/execute');

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBeNull();

    request.flush({ success: true, message: 'Ticket updated successfully', payload: {} });
  });
});
