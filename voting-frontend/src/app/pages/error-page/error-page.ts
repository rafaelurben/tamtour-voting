import { Component, input } from '@angular/core';

@Component({
  selector: 'app-error-page',
  imports: [],
  templateUrl: './error-page.html',
  styleUrl: './error-page.css',
})
export class ErrorPage {
  public errorMessage = input.required<string>();
  public errorDescription = input.required<string>();
}
