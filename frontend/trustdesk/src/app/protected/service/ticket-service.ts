import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUpdateTicketPriorityRequest, IUpdateTicketStatusRequest } from '../../interface/request';
import { ISingleTicketResponse, ITicketResponse, ITriageResponse } from '../../interface/response';
import { TicketPriority } from '../../enum/TicketPriority.enum';
import { TicketStatus } from '../../enum/TicketStatus.enum';

@Injectable({
    providedIn: 'root'
})
export class TicketService {
    private readonly baseURL = environment.apiUrl.replace(/\/$/, '');

    constructor(private readonly http: HttpClient) { }

    getAllTickets(): Observable<ITicketResponse> {
        return this.http.get<ITicketResponse>(`${this.baseURL}/tickets`);
    }

    getTicketById(ticketId: string): Observable<ISingleTicketResponse> {
        return this.http.get<ISingleTicketResponse>(`${this.baseURL}/tickets/${ticketId}`);
    }

    updateTicketStatus(ticketId: string, status: TicketStatus): Observable<ISingleTicketResponse> {
        const request: IUpdateTicketStatusRequest = { status };

        return this.http.patch<ISingleTicketResponse>(`${this.baseURL}/tickets/${ticketId}/status`, request);
    }

    updateTicketPriority(ticketId: string, priority: TicketPriority): Observable<ISingleTicketResponse> {
        const request: IUpdateTicketPriorityRequest = { priority };

        return this.http.patch<ISingleTicketResponse>(`${this.baseURL}/tickets/${ticketId}/priority`, request);
    }

    triageTicket(ticketId: string): Observable<ITriageResponse> {
        return this.http.post<ITriageResponse>(`${this.baseURL}/tickets/${ticketId}/triage`, null);
    }

}
