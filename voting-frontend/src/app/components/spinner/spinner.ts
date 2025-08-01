import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-spinner',
  imports: [],
  templateUrl: './spinner.html',
  styleUrl: './spinner.css',
})
export class Spinner {
  public size = input('1rem');
  public color = input('inherit');

  protected borderWidth = computed(() => `calc(${this.size()} / 7)`);
}
