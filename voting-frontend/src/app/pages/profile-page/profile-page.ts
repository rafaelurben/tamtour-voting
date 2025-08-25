import { Component, effect, inject, signal } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { NgOptimizedImage } from '@angular/common';
import { Button } from '../../components/button/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-profile-page',
  imports: [NgOptimizedImage, Button, RouterLink],
  templateUrl: './profile-page.html',
  styleUrl: './profile-page.css',
})
export class ProfilePage {
  private readonly DEFAULT_PROFILE_IMAGE = '/avatar-placeholder.svg';

  protected readonly authService = inject(AuthService);
  protected readonly profileImageUrl = signal(this.DEFAULT_PROFILE_IMAGE);

  constructor() {
    effect(() => {
      const user = this.authService.user();
      if (user?.pictureLink) {
        const newLink = user.pictureLink.replace('=s96-c', '=s256-c');
        this.profileImageUrl.set(newLink);
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
