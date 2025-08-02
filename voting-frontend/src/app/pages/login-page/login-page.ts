import { Component, signal } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { GoogleButton } from './google-button/google-button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-login-page',
  imports: [NgOptimizedImage, GoogleButton, RouterLink],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
})
export class LoginPage {
  protected readonly displayWhyGoogleText = signal(false);

  protected toggleWhyGoogleText() {
    this.displayWhyGoogleText.update(a => !a);
  }
}
