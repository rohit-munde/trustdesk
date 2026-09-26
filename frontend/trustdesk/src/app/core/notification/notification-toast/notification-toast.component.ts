import { Component, Input, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { INotificationItem } from '../notification.model';
import { NotificationService } from '../notification.service';
import { Clipboard } from '@angular/cdk/clipboard';

@Component({
  selector: 'app-notification-toast',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './notification-toast.component.html',
  styleUrl: './notification-toast.component.scss'
})
export class NotificationToastComponent {
  @Input({ required: true }) notification!: INotificationItem;

  private notificationService = inject(NotificationService);
  private clipboard = inject(Clipboard);
  private cdr = inject(ChangeDetectorRef);

  isCopied = false;

  close(): void {
    this.notificationService.remove(this.notification.id);
  }

  copyError(): void {
    if (this.notification.errorResponse) {
      this.clipboard.copy(JSON.stringify(this.notification.errorResponse, null, 2));
      this.isCopied = true;
      setTimeout(() => {
        this.isCopied = false;
        this.cdr.markForCheck();
      }, 1300);
    }
  }

  getIconName(): string {
    switch (this.notification.type) {
      case 'success': return 'check_circle';
      case 'error': return 'error';
      case 'warning': return 'warning';
      case 'info': return 'info';
      default: return 'info';
    }
  }

  getNotificationTitle(): string {
    switch (this.notification.type) {
      case 'success': return 'Success';
      case 'error': return 'Error';
      case 'warning': return 'Warning';
      case 'info': return 'Info';
      default: return 'Info';
    }
  }
}
