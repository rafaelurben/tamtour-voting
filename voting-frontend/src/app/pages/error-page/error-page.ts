import { Component, input } from '@angular/core';
import { Button } from '../../components/button/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-error-page',
  imports: [Button, RouterLink],
  templateUrl: './error-page.html',
})
export class ErrorPage {
  public errorMessage = input.required<string>();
  public errorDescription = input.required<string>();
  public backUrl = input<string | null>(null);
}
