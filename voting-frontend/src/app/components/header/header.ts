import { Component, inject } from '@angular/core';
import { AuthService } from '../../api/auth.service';
import { RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [RouterLink, NgOptimizedImage],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  protected readonly authService = inject(AuthService);
}
