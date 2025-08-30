import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { RouterLink } from '@angular/router';
import { Button } from '../../components/button/button';
import { Spinner } from '../../components/spinner/spinner';

@Component({
  selector: 'app-logout-page',
  imports: [RouterLink, Button, Spinner],
  templateUrl: './logout-page.html',
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
