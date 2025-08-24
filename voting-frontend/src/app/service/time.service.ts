import { Injectable } from '@angular/core';
import { interval, map } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

@Injectable({ providedIn: 'root' })
export class TimeService {
  public readonly currentTime1s = toSignal(
    interval(1000).pipe(map(() => new Date())),
    { initialValue: new Date() }
  );
}
