import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { IApiErrorResponse } from '../interface/response';
import { inject } from '@angular/core';
import { NotificationService } from '../core/notification/notification.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const apiError: IApiErrorResponse | undefined = error.error;

      const message = apiError?.message || apiError?.error || error?.message || "An unexpected error has occured!";

      notificationService.showError(message, apiError);

      return throwError(() => error);
    })
  );
};
