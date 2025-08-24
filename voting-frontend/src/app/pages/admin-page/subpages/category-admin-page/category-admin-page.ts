import { Component, inject, input, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AdminVotingCategoriesApi } from '../../../../api/admin/admin-voting-categories.api';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { VotingCandidateDto } from '../../../../dto/voting-candidate.dto';
import { DatePipe } from '@angular/common';
import { TimeRemaining } from '../../../../components/time-remaining/time-remaining';
import { Spinner } from '../../../../components/spinner/spinner';

@Component({
  selector: 'app-category-admin-page',
  imports: [DatePipe, TimeRemaining, Spinner],
  templateUrl: './category-admin-page.html',
  styleUrl: './category-admin-page.css',
})
export class CategoryAdminPage implements OnInit {
  private readonly categoriesApi = inject(AdminVotingCategoriesApi);
  private readonly router = inject(Router);

  public readonly categoryId = input.required<number>();

  protected category = signal<VotingCategoryBaseDto | null>(null);
  protected candidates = signal<VotingCandidateDto[] | null>(null);

  ngOnInit(): void {
    this.fetchData();
  }

  protected fetchData() {
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
    this.categoriesApi.getCategoryCandidates(this.categoryId()).subscribe({
      next: data =>
        this.candidates.set(
          data.toSorted((a, b) => a.startNumber.localeCompare(b.startNumber))
        ),
    });
  }
}
