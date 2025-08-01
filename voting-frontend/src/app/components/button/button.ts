import { Component, input, output } from '@angular/core';
import { Spinner } from '../spinner/spinner';

@Component({
  selector: 'app-button',
  imports: [Spinner],
  templateUrl: './button.html',
  styleUrl: './button.css',
})
export class Button {
  public type = input<'button' | 'submit' | 'reset'>('button');
  public loading = input<boolean>(false);
  public disabled = input<boolean>(false);

  public trigger = output();
}
