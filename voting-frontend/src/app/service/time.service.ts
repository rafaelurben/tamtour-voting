import { Injectable } from '@angular/core';
import { interval, map } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

@Injectable({ providedIn: 'root' })
export class TimeService {
  public readonly currentTime1s = toSignal(
    interval(1000).pipe(map(() => new Date())),
    { initialValue: new Date() }
  );

  /**
   * Convert a timezone-aware datetime string into a string usable in the 'datetime-local' input.
   * @param datetime ISO datetime string including timezone
   * @returns string in 'yyyy-MM-ddTHH:mm' format in the local timezone
   */
  public toDateTimeInputFormat(datetime: string): string {
    const parsedDate = new Date(datetime);
    parsedDate.setMinutes(
      parsedDate.getMinutes() - parsedDate.getTimezoneOffset()
    );
    return parsedDate.toISOString().slice(0, 16);
  }
}
