import { ErrorHandler, inject, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerService implements ErrorHandler {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  handleError(error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 401) {
        this.authService
          .logout()
          .subscribe(() => this.router.navigate(['/login']));
        alert('Du wurdest ausgeloggt. Bitte melde dich erneut an.');
      } else if (error.status >= 400 && error.status < 500) {
        alert(`Ein Fehler ist aufgetreten: ${error.status} ${error.error}`);
      } else if (error.status >= 500) {
        alert(
          `Ein Serverfehler ist aufgetreten. Bitte versuche es später erneut. Details: ${error.status} ${error.error}`
        );
      }
      return;
    }
    throw error;
  }
}
