import { Component, inject } from '@angular/core';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { map, Observable } from 'rxjs';
import { AdminVotingCategoriesApi } from '../../../../api/admin/admin-voting-categories.api';
import { Spinner } from '../../../../components/spinner/spinner';
import { AsyncPipe, DatePipe } from '@angular/common';
import { TimeRemaining } from '../../../../components/time-remaining/time-remaining';
import { Button } from '../../../../components/button/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-categories-admin-page',
  imports: [Spinner, AsyncPipe, DatePipe, TimeRemaining, Button, RouterLink],
  templateUrl: './categories-admin-page.html',
  styleUrl: './categories-admin-page.css',
})
export class CategoriesAdminPage {
  protected categories$!: Observable<VotingCategoryBaseDto[]>;

  private readonly votingCategoriesApi = inject(AdminVotingCategoriesApi);

  constructor() {
    this.categories$ = this.votingCategoriesApi
      .getCategories()
      .pipe(map(c => c.toSorted((a, b) => a.id - b.id)));
  }
}
