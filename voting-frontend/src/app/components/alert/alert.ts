import { Component, input } from '@angular/core';

@Component({
  selector: 'app-alert',
  imports: [],
  templateUrl: './alert.html',
  styleUrl: './alert.css',
})
export class Alert {
  public type = input.required<'success' | 'warning' | 'danger' | 'info'>();
  public title = input.required<string>();
  public message = input.required<string>();
}
