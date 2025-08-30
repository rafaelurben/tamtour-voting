import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WhoamiDto } from '../dto/whoami.dto';
import { VotingUserDto } from '../dto/voting-user.dto';
import { UpdateMeDto } from '../dto/update-me.dto';

@Injectable({ providedIn: 'root' })
export class AuthApi {
  private readonly http = inject(HttpClient);

  whoami(): Observable<WhoamiDto> {
    return this.http.get<WhoamiDto>('/api/whoami');
  }

  updateMe(update: UpdateMeDto): Observable<VotingUserDto> {
    return this.http.patch<VotingUserDto>('/api/me', update);
  }

  logout(): Observable<void> {
    return this.http.post<void>('/api/logout', { withCredentials: true });
  }
}
