import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VotingUserDto } from '../../dto/voting-user.dto';

@Injectable({ providedIn: 'root' })
export class AdminVotingUsersApi {
  private readonly http = inject(HttpClient);

  getUsers(): Observable<VotingUserDto[]> {
    return this.http.get<VotingUserDto[]>('/api/admin/users');
  }

  blockUser(userId: number): Observable<VotingUserDto> {
    return this.http.post<VotingUserDto>(
      `/api/admin/users/${userId}/block`,
      {}
    );
  }

  unblockUser(userId: number): Observable<VotingUserDto> {
    return this.http.post<VotingUserDto>(
      `/api/admin/users/${userId}/unblock`,
      {}
    );
  }
}
