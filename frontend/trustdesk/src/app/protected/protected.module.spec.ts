import { DashboardComponent } from './dashboard/dashboard.component';
import { protectedRoutes } from './protected.module';
import { TicketsComponent } from './tickets/tickets.component';

describe('protectedRoutes', () => {
  it('uses DashboardComponent as the protected route shell', () => {
    expect(protectedRoutes).toEqual(
      expect.arrayContaining([
        expect.objectContaining({
          path: '',
          component: DashboardComponent,
        }),
      ])
    );
  });

  it('routes tickets as a child inside the dashboard shell', () => {
    const dashboardShellRoute = protectedRoutes.find((route) => route.path === '');

    expect(dashboardShellRoute?.children).toEqual(
      expect.arrayContaining([
        expect.objectContaining({
          path: 'tickets',
          component: TicketsComponent,
        }),
      ])
    );
  });
});
