import { routes } from './app.routes';

describe('routes', () => {
  it('lazy-loads protected routes under dashboard', () => {
    const dashboardRoute = routes.find((route) => route.path === 'dashboard');

    expect(dashboardRoute?.loadChildren).toBeTruthy();
  });

  it('does not define dashboard child paths as flat root routes', () => {
    expect(routes.some((route) => route.path === 'dashboard/tickets')).toBe(false);
  });

  it('redirects the empty root path to dashboard only', () => {
    expect(routes).toEqual(
      expect.arrayContaining([
        expect.objectContaining({
          path: '',
          redirectTo: 'dashboard',
          pathMatch: 'full',
        }),
      ])
    );
  });
});
