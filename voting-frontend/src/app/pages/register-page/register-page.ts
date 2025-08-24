import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router, RouterLink } from '@angular/router';
import { Button } from '../../components/button/button';

@Component({
  selector: 'app-register-page',
  imports: [ReactiveFormsModule, Button, RouterLink],
  templateUrl: './register-page.html',
  styleUrl: './register-page.css',
})
export class RegisterPage {
  private readonly formBuilder = inject(FormBuilder);
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected nameForm = this.formBuilder.nonNullable.group({
    firstName: [this.authService.user()?.firstName || '', Validators.required],
    lastName: [this.authService.user()?.lastName || '', Validators.required],
    confirmRules: [false, Validators.requiredTrue],
  });

  protected requestInProgress = signal(false);

  protected onSubmit() {
    if (this.nameForm.invalid) return;
    const { firstName, lastName } = this.nameForm.getRawValue();
    this.requestInProgress.set(true);
    this.authService.updateMe({ firstName, lastName }).subscribe({
      next: () => {
        this.requestInProgress.set(false);
        this.router.navigate(['/']);
      },
      error: error => {
        this.requestInProgress.set(false);
        throw error;
      },
    });
  }
}
