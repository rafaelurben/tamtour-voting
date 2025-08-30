import { Component, inject, signal } from '@angular/core';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { map, Observable } from 'rxjs';
import { AdminVotingCategoriesApi } from '../../../../api/admin/admin-voting-categories.api';
import { Spinner } from '../../../../components/spinner/spinner';
import { AsyncPipe, DatePipe } from '@angular/common';
import { TimeRemaining } from '../../../../components/time-remaining/time-remaining';
import { Button } from '../../../../components/button/button';
import { RouterLink } from '@angular/router';
import { CategoryForm } from '../../../../components/admin/category-form/category-form';
import { VotingCategoryRequestDto } from '../../../../dto/admin/voting-category-request.dto';

@Component({
  selector: 'app-categories-admin-page',
  imports: [
    Spinner,
    AsyncPipe,
    DatePipe,
    TimeRemaining,
    Button,
    RouterLink,
    CategoryForm,
  ],
  templateUrl: './categories-admin-page.html',
})
export class CategoriesAdminPage {
  protected categories$!: Observable<VotingCategoryBaseDto[]>;

  private readonly votingCategoriesApi = inject(AdminVotingCategoriesApi);

  private loadCategories() {
    this.categories$ = this.votingCategoriesApi
      .getCategories()
      .pipe(map(c => c.toSorted((a, b) => a.id - b.id)));
  }

  constructor() {
    this.loadCategories();
  }

  protected addFormVisible = signal(false);
  protected addInProgress = signal(false);

  protected addFormSubmit(category: VotingCategoryRequestDto) {
    this.addInProgress.set(true);
    this.votingCategoriesApi.createCategory(category).subscribe({
      next: () => {
        this.addInProgress.set(false);
        this.addFormVisible.set(false);
        this.loadCategories();
      },
      error: error => {
        this.addInProgress.set(false);
        throw error;
      },
    });
  }
}
