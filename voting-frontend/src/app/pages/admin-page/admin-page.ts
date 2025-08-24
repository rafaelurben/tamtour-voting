import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-page',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css',
})
export class AdminPage {}
