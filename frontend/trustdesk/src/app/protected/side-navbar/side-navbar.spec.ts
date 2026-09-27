import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { provideRouter, RouterLink } from '@angular/router';
import { SideNavbar } from './side-navbar';

describe('SideNavbar', () => {
  let component: SideNavbar;
  let fixture: ComponentFixture<SideNavbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SideNavbar],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(SideNavbar);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the TrustDesk navigation items', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('TrustDesk');
    expect(compiled.textContent).toContain('Tickets');
    expect(compiled.textContent).toContain('Evaluations');
  });

  it('should use Angular Material sidenav and nav list components', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelector('mat-sidenav-container')).toBeTruthy();
    expect(compiled.querySelector('mat-sidenav')).toBeTruthy();
    expect(compiled.querySelector('mat-nav-list')).toBeTruthy();
    expect(compiled.querySelectorAll('a[mat-list-item]').length).toBe(2);
  });

  it('should link Tickets to the dashboard tickets route', () => {
    const ticketsLink = fixture.debugElement
      .queryAll(By.directive(RouterLink))
      .find((link) => link.nativeElement.textContent.includes('Tickets'));

    if (!ticketsLink) {
      throw new Error('Tickets router link was not rendered');
    }

    const routerLink = ticketsLink.injector.get(RouterLink);

    expect(routerLink.href).toBe('/dashboard/tickets');
  });
});
