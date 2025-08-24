import { Component, computed, inject, input } from '@angular/core';
import { TimeService } from '../../service/time.service';

@Component({
  selector: 'app-time-remaining',
  imports: [],
  templateUrl: './time-remaining.html',
  styleUrl: './time-remaining.css',
})
export class TimeRemaining {
  private readonly timeService = inject(TimeService);

  public target = input.required<Date | string>();
  public currentOverride = input<Date>();

  protected timeRemainingS = computed(() => {
    const current = this.currentOverride() || this.timeService.currentTime1s();
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
