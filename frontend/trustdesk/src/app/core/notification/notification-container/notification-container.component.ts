import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../notification.service';
import { NotificationToastComponent } from '../notification-toast/notification-toast.component';

@Component({
  selector: 'app-notification-container',
  standalone: true,
  imports: [CommonModule, NotificationToastComponent],
  templateUrl: './notification-container.component.html',
  styleUrl: './notification-container.component.scss'
})
export class NotificationContainerComponent {
  public notificationService = inject(NotificationService);
}
