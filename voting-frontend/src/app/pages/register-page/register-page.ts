import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../api/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-page',
  imports: [ReactiveFormsModule],
  templateUrl: './register-page.html',
  styleUrl: './register-page.css',
})
export class RegisterPage {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected nameForm = this.formBuilder.nonNullable.group({
    firstName: [this.authService.user()?.firstName || '', Validators.required],
    lastName: [this.authService.user()?.lastName || '', Validators.required],
  });

  protected onSubmit() {
    if (this.nameForm.invalid) return;
    const { firstName, lastName } = this.nameForm.getRawValue();
    this.authService.updateMe({ firstName, lastName }).subscribe({
      next: () => {
        this.router.navigate(['/app']);
      },
    });
  }
}
