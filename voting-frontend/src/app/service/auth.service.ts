import { computed, inject, Injectable, OnDestroy, signal } from '@angular/core';
import { WhoamiDto } from '../dto/whoami.dto';
import { VotingUserDto } from '../dto/voting-user.dto';
import { UpdateMeDto } from '../dto/update-me.dto';
import { AuthApi } from '../api/auth.api';
import { catchError, filter, Observable, of, take, tap, timeout } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService implements OnDestroy {
  private readonly authApi = inject(AuthApi);

  private readonly whoami = signal<WhoamiDto | null | undefined>(undefined);

  public error = signal<string | null>(null);
  public isLoading = computed(() => this.whoami() === undefined);
  public user = computed(() => this.whoami()?.user);
  public isAdmin = computed(() => this.whoami()?.isAdmin);

  private readonly intervalId: number;

  constructor() {
    // Refresh every 5 minutes to keep session alive
    this.intervalId = setInterval(
      () => {
        this.fetchWhoami().subscribe();
      },
      5 * 60 * 1000
    );
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }

  fetchWhoami(): Observable<WhoamiDto | null> {
    return this.authApi.whoami().pipe(
      timeout(5000),
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          // not logged in
          return of(null);
        } else if (error.name === 'TimeoutError') {
          this.error.set(
            'Der Server kann derzeit nicht erreicht werden. Versuche es erneut.'
          );
        } else {
          this.error.set('Unbekannter Fehler: ' + error.message);
        }
        return of(null);
      }),
      tap(data => {
        this.whoami.set(data);
        if (data) {
          this.error.set(null);
        }
      })
    );
  }

  getWhoami(): Observable<WhoamiDto | null> {
    if (this.isLoading()) {
      return toObservable(this.whoami).pipe(
        filter(data => data !== undefined),
        take(1)
      );
    }
    return of(this.whoami()!);
  }

  updateMe(update: UpdateMeDto): Observable<VotingUserDto> {
    return this.authApi
      .updateMe(update)
      .pipe(
        tap(user =>
          this.whoami.update(current => (current ? { ...current, user } : null))
        )
      );
  }

  logout(): Observable<void> {
    return this.authApi.logout().pipe(
      tap(() => {
        this.whoami.set(null);
      })
    );
  }
}
