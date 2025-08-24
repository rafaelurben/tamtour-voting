import { Component, inject } from '@angular/core';
import { VotingUserDto } from '../../../../dto/voting-user.dto';
import { Observable } from 'rxjs';
import { AdminVotingUsersApi } from '../../../../api/admin/admin-voting-users.api';
import { Spinner } from '../../../../components/spinner/spinner';
import {
  AsyncPipe,
  DatePipe,
  NgOptimizedImage,
  NgStyle,
} from '@angular/common';
import { Button } from '../../../../components/button/button';

@Component({
  selector: 'app-user-admin-page',
  imports: [Spinner, AsyncPipe, DatePipe, NgOptimizedImage, NgStyle, Button],
  templateUrl: './user-admin-page.html',
  styleUrl: './user-admin-page.css',
})
export class UserAdminPage {
  protected users$!: Observable<VotingUserDto[]>;
  protected blockButtonLoadingIds: number[] = [];

  private readonly votingUsersApi = inject(AdminVotingUsersApi);

  constructor() {
    this.users$ = this.votingUsersApi.getUsers();
  }

  protected blockUser(userId: number): void {
    this.blockButtonLoadingIds.push(userId);
    this.votingUsersApi.blockUser(userId).subscribe(() => {
      this.users$ = this.votingUsersApi.getUsers();
      this.blockButtonLoadingIds.splice(
        this.blockButtonLoadingIds.indexOf(userId),
        1
      );
    });
  }

  protected unblockUser(userId: number): void {
    this.blockButtonLoadingIds.push(userId);
    this.votingUsersApi.unblockUser(userId).subscribe(() => {
      this.users$ = this.votingUsersApi.getUsers();
      this.blockButtonLoadingIds.splice(
        this.blockButtonLoadingIds.indexOf(userId),
        1
      );
    });
  }
}
