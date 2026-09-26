import { routes } from './app.routes';
import { DashboardComponent } from './protected/dashboard/dashboard.component';

describe('routes', () => {
  it('routes dashboard path to DashboardComponent', () => {
    expect(routes).toEqual(
      expect.arrayContaining([
        expect.objectContaining({
          path: 'dashboard',
          component: DashboardComponent,
        }),
      ])
    );
  });
});
