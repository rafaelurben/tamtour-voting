import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-time-remaining',
  imports: [],
  templateUrl: './time-remaining.html',
  styleUrl: './time-remaining.css',
})
export class TimeRemaining {
  public target = input.required<Date | string>();
  public currentOverride = input<Date>();

  protected timeRemainingS = computed(() => {
    const current = this.currentOverride() || new Date();
    return (new Date(this.target()).getTime() - current.getTime()) / 1000;
  });

  protected timeRemainingDisplay = computed(() => {
    const timeLeft = this.timeRemainingS();

    const seconds = Math.floor(timeLeft % 60);
    const minutes = Math.floor((timeLeft / 60) % 60);
    const hours = Math.floor((timeLeft / (60 * 60)) % 24);
    const days = Math.floor(timeLeft / (60 * 60 * 24));

    return `${days}d ${hours}h ${minutes}m ${seconds}s`;
  });
}
