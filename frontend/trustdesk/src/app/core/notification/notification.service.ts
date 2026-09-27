import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { INotificationItem, NotificationType } from './notification.model';
import { IApiErrorResponse } from '../../interface/response';


@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<INotificationItem[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private idCounter = 0;

  constructor() { }

  get notifications(): INotificationItem[] {
    return this.notificationsSubject.value;
  }

  showSuccess(message: string, duration: number = 3000): void {
    this.addNotification({
      type: 'success',
      message,
      isAutoClose: true,
      duration,
    });
  }

  showError(message: string, errorResponse?: IApiErrorResponse, duration: number = 5000): void {
    this.addNotification({
      type: 'error',
      message,
      errorResponse,
      isAutoClose: true,
      duration,
    });
  }

  showWarning(message: string, duration: number = 4000): void {
    this.addNotification({
      type: 'warning',
      message,
      isAutoClose: true,
      duration,
    });
  }

  showInfo(message: string, duration: number = 3000): void {
    this.addNotification({
      type: 'info',
      message,
      isAutoClose: true,
      duration,
    });
  }

  private addNotification(options: Omit<INotificationItem, 'id' | 'count'>): void {
    const currentNotifications = this.notifications;

    // Check for duplicate based on type and message
    const duplicateIndex = currentNotifications.findIndex(
      (n) => n.type === options.type && n.message === options.message
    );

    if (duplicateIndex > -1) {
      // Increment count for duplicate
      const duplicate = { ...currentNotifications[duplicateIndex] };
      duplicate.count += 1;

      const newNotifications = [...currentNotifications];
      newNotifications[duplicateIndex] = duplicate;
      this.notificationsSubject.next(newNotifications);
    } else {
      // Add new notification
      const newNotification: INotificationItem = {
        id: ++this.idCounter,
        count: 1,
        ...options
      };

      this.notificationsSubject.next([...currentNotifications, newNotification]);

      if (newNotification.isAutoClose) {
        setTimeout(() => {
          this.remove(newNotification.id);
        }, newNotification.duration);
      }
    }
  }

  remove(id: number): void {
    const newNotifications = this.notifications.filter((n) => n.id !== id);
    this.notificationsSubject.next(newNotifications);
  }

  clearAll(): void {
    this.notificationsSubject.next([]);
  }
}
