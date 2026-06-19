import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snack = inject(MatSnackBar);
  const auth = inject(AuthService);
  return next(req).pipe(catchError((err: HttpErrorResponse) => {
    const message = err.error?.message || err.message || 'Request failed';
    snack.open(message, 'Close', { duration: 3500 });
    if (err.status === 401) auth.logout();
    return throwError(() => err);
  }));
};
