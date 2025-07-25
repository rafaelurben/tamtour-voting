import { computed, inject, Injectable, signal } from '@angular/core';
import { WhoamiDto } from '../dto/whoami.dto';
import { VotingUserDto } from '../dto/voting-user.dto';
import { UpdateMeDto } from '../dto/update-me.dto';
import { AuthApi } from './auth.api';
import { catchError, Observable, of } from 'rxjs';

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

    const obs = this.authApi.whoami().pipe(catchError(() => of(null)));
    obs.subscribe({
      next: data => {
        this.whoami.set(data);
        this.loading.set(false);
      },
    });
    return obs;
  }

  updateMe(update: UpdateMeDto): Observable<VotingUserDto> {
    const obs = this.authApi.updateMe(update);
    obs.subscribe({
      next: user =>
        this.whoami.update(current =>
          current === null ? null : { ...current, user }
        ),
    });
    return obs;
  }

  logout(): Observable<void> {
    const obs = this.authApi.logout();
    obs.subscribe({
      next: () => {
        this.whoami.set(null);
        this.loading.set(false);
      },
    });
    return obs;
  }
}
