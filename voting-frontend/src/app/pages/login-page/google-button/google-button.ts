import { Component } from '@angular/core';

@Component({
  selector: 'app-google-button',
  imports: [],
  templateUrl: './google-button.html',
  styleUrl: './google-button.css',
})
export class GoogleButton {
  protected onClick() {
    window.location.href = '/api/oauth2/authorization/google';
  }
}
