import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
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
});
