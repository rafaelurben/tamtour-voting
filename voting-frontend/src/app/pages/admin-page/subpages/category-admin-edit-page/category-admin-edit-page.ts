import { Component, inject, input, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AdminVotingCategoriesApi } from '../../../../api/admin/admin-voting-categories.api';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { VotingCandidateDto } from '../../../../dto/voting-candidate.dto';
import { DatePipe } from '@angular/common';
import { TimeRemaining } from '../../../../components/time-remaining/time-remaining';
import { Spinner } from '../../../../components/spinner/spinner';
import { Button } from '../../../../components/button/button';
import { CategoryForm } from '../../../../components/admin/category-form/category-form';
import { VotingCategoryRequestDto } from '../../../../dto/admin/voting-category-request.dto';
import { CandidateForm } from '../../../../components/admin/candidate-form/candidate-form';
import { VotingCandidateRequestDto } from '../../../../dto/admin/voting-candidate-request.dto';

@Component({
  selector: 'app-category-admin-page',
  imports: [
    DatePipe,
    TimeRemaining,
    Spinner,
    Button,
    CategoryForm,
    CandidateForm,
  ],
  templateUrl: './category-admin-edit-page.html',
})
export class CategoryAdminEditPage implements OnInit {
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

  // Edit category

  protected editFormVisible = signal(false);
  protected editInProgress = signal(false);

  protected editFormSubmit(category: VotingCategoryRequestDto) {
    this.editInProgress.set(true);
    this.categoriesApi.updateCategory(this.categoryId(), category).subscribe({
      next: () => {
        this.editInProgress.set(false);
        this.editFormVisible.set(false);
        this.fetchData();
      },
      error: error => {
        this.editInProgress.set(false);
        throw error;
      },
    });
  }

  // Edit candidate

  protected editedCandidate = signal<VotingCandidateDto | null>(null);
  protected editCandidateFormVisible = signal(false);
  protected editCandidateInProgress = signal(false);

  protected editCandidateFormSubmit(candidate: VotingCandidateRequestDto) {
    this.editCandidateInProgress.set(true);
    this.categoriesApi
      .updateCandidate(this.categoryId(), this.editedCandidate()!.id, candidate)
      .subscribe({
        next: () => {
          this.editCandidateInProgress.set(false);
          this.editCandidateFormVisible.set(false);
          this.fetchData();
        },
        error: error => {
          this.editCandidateInProgress.set(false);
          throw error;
        },
      });
  }

  protected editCandidate(candidate: VotingCandidateDto) {
    this.editedCandidate.set(candidate);
    this.editCandidateFormVisible.set(true);
  }

  // Add candidate

  protected addCandidateFormVisible = signal(false);
  protected addCandidateInProgress = signal(false);

  protected addCandidateFormSubmit(candidate: VotingCandidateRequestDto) {
    this.addCandidateInProgress.set(true);
    this.categoriesApi.addCandidate(this.categoryId(), candidate).subscribe({
      next: () => {
        this.addCandidateInProgress.set(false);
        this.addCandidateFormVisible.set(false);
        this.fetchData();
      },
      error: error => {
        this.addCandidateInProgress.set(false);
        throw error;
      },
    });
  }
}
