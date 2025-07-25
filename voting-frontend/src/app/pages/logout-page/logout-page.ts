import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthService } from '../../api/auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-logout-page',
  imports: [RouterLink],
  templateUrl: './logout-page.html',
  styleUrl: './logout-page.css',
})
export class LogoutPage implements OnInit {
  private readonly authService = inject(AuthService);

  protected isDone = signal(false);

  ngOnInit(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.isDone.set(true);
      },
    });
  }
}
