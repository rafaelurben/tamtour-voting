import { Component, inject, input, signal, OnInit } from '@angular/core';
import { AdminVotingCategoriesApi } from '../../../../api/admin/admin-voting-categories.api';
import { Router } from '@angular/router';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { VotingCandidateDto } from '../../../../dto/voting-candidate.dto';
import { HttpErrorResponse } from '@angular/common/http';
import { VotingSetDto } from '../../../../dto/admin/voting-set.dto';
import { DatePipe } from '@angular/common';
import { Spinner } from '../../../../components/spinner/spinner';
import { TimeRemaining } from '../../../../components/time-remaining/time-remaining';
import { CandidateSetsTable } from './candidate-sets-table/candidate-sets-table';
import { Button } from '../../../../components/button/button';
import { VotingCategoryResultDto } from '../../../../dto/admin/voting-category-result.dto';

@Component({
  selector: 'app-category-admin-votes-page',
  imports: [DatePipe, Spinner, TimeRemaining, CandidateSetsTable, Button],
  templateUrl: './category-admin-votes-page.html',
})
export class CategoryAdminVotesPage implements OnInit {
  private readonly categoriesApi = inject(AdminVotingCategoriesApi);
  private readonly router = inject(Router);

  public readonly categoryId = input.required<number>();

  protected category = signal<VotingCategoryBaseDto | null>(null);
  protected candidates = signal<VotingCandidateDto[] | null>(null);
  protected sets = signal<VotingSetDto[] | null>(null);
  protected result = signal<VotingCategoryResultDto | null>(null);

  protected isFetchingSets = signal(false);
  protected isFetchingResult = signal(false);

  ngOnInit(): void {
    this.fetchCategory();
    this.fetchCandidates();
    this.fetchSets();
    this.fetchResult();
  }

  protected fetchCategory() {
    this.categoriesApi.getCategory(this.categoryId()).subscribe({
      next: data => this.category.set(data),
      error: (err: HttpErrorResponse) => {
        if (err.status === 404) {
          this.router.navigate(['/error'], {
            skipLocationChange: true,
            queryParams: {
              errorMessage: 'Kategorie nicht gefunden',
              errorDescription:
                'Diese Kategorie existiert nicht oder ist nicht mehr verfügbar.',
              backUrl: `/`,
            },
          });
        } else {
          throw err;
        }
      },
    });
  }

  protected fetchCandidates() {
    this.categoriesApi.getCategoryCandidates(this.categoryId()).subscribe({
      next: data =>
        this.candidates.set(
          data.toSorted((a, b) => a.startNumber.localeCompare(b.startNumber))
        ),
    });
  }

  protected fetchSets() {
    this.isFetchingSets.set(true);
    this.categoriesApi.getSets(this.categoryId()).subscribe({
      next: data => {
        this.sets.set(data);
        this.isFetchingSets.set(false);
      },
      error: err => {
        this.isFetchingSets.set(false);
        throw err;
      },
    });
  }

  protected fetchResult() {
    this.isFetchingResult.set(true);
    this.categoriesApi.getCategoryResult(this.categoryId()).subscribe({
      next: data => {
        this.result.set(data);
        this.isFetchingResult.set(false);
      },
      error: err => {
        this.isFetchingResult.set(false);
        throw err;
      },
    });
  }

  protected replaceSet(newSet: VotingSetDto) {
    this.sets.update(sets => sets!.map(s => (s.id === newSet.id ? newSet : s)));
  }
}
