import { Component, effect, inject, signal } from '@angular/core';
import { AuthService } from '../../api/auth.service';
import { RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { Spinner } from '../spinner/spinner';

@Component({
  selector: 'app-header',
  imports: [RouterLink, NgOptimizedImage, Spinner],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private readonly DEFAULT_PROFILE_IMAGE = '/avatar-placeholder.svg';

  protected readonly authService = inject(AuthService);
  protected readonly profileImageUrl = signal(this.DEFAULT_PROFILE_IMAGE);

  constructor() {
    effect(() => {
      const user = this.authService.user();
      if (user?.pictureLink) {
        this.profileImageUrl.set(user.pictureLink);
      } else {
        this.profileImageUrl.set(this.DEFAULT_PROFILE_IMAGE);
      }
    });
  }

  onProfileImageError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = this.DEFAULT_PROFILE_IMAGE;
  }
}
