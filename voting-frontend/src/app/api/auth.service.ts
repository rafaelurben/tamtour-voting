import { computed, inject, Injectable, signal } from '@angular/core';
import { WhoamiDto } from '../dto/whoami.dto';
import { VotingUserDto } from '../dto/voting-user.dto';
import { UpdateMeDto } from '../dto/update-me.dto';
import { AuthApi } from './auth.api';
import { catchError, Observable, of, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authApi = inject(AuthApi);

  private whoami = signal<WhoamiDto | null>(null);
  private loading = signal<boolean>(true);

  public isLoading = this.loading.asReadonly();
  public user = computed(() => this.whoami()?.user);
  public isAdmin = computed(() => this.whoami()?.isAdmin);

  getWhoami(): Observable<WhoamiDto | null> {
    const storedVal = this.whoami();
    if (storedVal !== null) {
      return of(storedVal);
    }

    return this.authApi.whoami().pipe(
      catchError(() => of(null)),
      tap(data => {
        this.whoami.set(data);
        this.loading.set(false);
      })
    );
  }

  updateMe(update: UpdateMeDto): Observable<VotingUserDto> {
    return this.authApi
      .updateMe(update)
      .pipe(
        tap(user =>
          this.whoami.update(current =>
            current === null ? null : { ...current, user }
          )
        )
      );
  }

  logout(): Observable<void> {
    return this.authApi.logout().pipe(
      tap(() => {
        this.whoami.set(null);
        this.loading.set(false);
      })
    );
  }
}
