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

@Component({
  selector: 'app-category-admin-votes-page',
  imports: [DatePipe, Spinner, TimeRemaining, CandidateSetsTable, Button],
  templateUrl: './category-admin-votes-page.html',
  styleUrl: './category-admin-votes-page.css',
})
export class CategoryAdminVotesPage implements OnInit {
  private readonly categoriesApi = inject(AdminVotingCategoriesApi);
  private readonly router = inject(Router);

  public readonly categoryId = input.required<number>();

  protected category = signal<VotingCategoryBaseDto | null>(null);
  protected candidates = signal<VotingCandidateDto[] | null>(null);
  protected sets = signal<VotingSetDto[] | null>(null);

  ngOnInit(): void {
    this.fetchCategory();
    this.fetchCandidates();
    this.fetchSets();
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
    this.categoriesApi.getSets(this.categoryId()).subscribe({
      next: data => this.sets.set(data),
    });
  }

  protected replaceSet(newSet: VotingSetDto) {
    this.sets.update(sets => sets!.map(s => (s.id === newSet.id ? newSet : s)));
  }
}
