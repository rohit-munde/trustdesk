import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TicketsComponent } from './tickets/tickets.component';

export const protectedRoutes: Routes = [
    {
        path: '',
        component: DashboardComponent,
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'tickets',
            },
            {
                path: 'tickets',
                component: TicketsComponent,
            },
            {
                path: 'evaluations',
                component: TicketsComponent,
            },
        ],
    },
];

@NgModule({
    imports: [
        DashboardComponent,
        TicketsComponent,
        RouterModule.forChild(protectedRoutes),
    ],
    exports: [RouterModule],
})
export class ProtectedModule { }
