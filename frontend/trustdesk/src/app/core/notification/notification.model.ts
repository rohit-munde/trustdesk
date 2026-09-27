import { IApiErrorResponse } from "../../interface/response";

export type NotificationType = 'success' | 'error' | 'warning' | 'info';

export interface INotificationItem {
  id: number;
  type: NotificationType;
  message: string;
  count: number;
  errorResponse?: IApiErrorResponse;
  duration: number;
  isAutoClose: boolean;
}
