import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { NotificationService } from '../../core/notification/notification.service';
import { SideNavbar } from '../side-navbar/side-navbar';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  standalone: true,
  imports: [SideNavbar, RouterOutlet]
})
export class DashboardComponent implements OnInit {
  private router = inject(Router);

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    // this.dashboardService.getDashboard().subscribe({
    //   next: (response: IDashboardResponse) => { this.notificationService.showSuccess(response.message) }
    // })
  }

  onLogout(): void {
    console.log('Clearing token and redirecting to login...');
    localStorage.removeItem('token');
    this.notificationService.showSuccess('Logged out successfully.');
    this.router.navigate(['/']);
  }
}
