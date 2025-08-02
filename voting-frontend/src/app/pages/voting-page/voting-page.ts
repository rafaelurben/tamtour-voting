import { Component, inject, input, OnInit, signal } from '@angular/core';
import { VotingCategoryApi } from '../../api/voting-category.api';
import { VotingCategoryUserDetailDto } from '../../dto/voting-category-user-detail.dto';
import { CategoryVote } from './category-vote/category-vote';
import { Spinner } from '../../components/spinner/spinner';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-voting-page',
  imports: [CategoryVote, Spinner],
  templateUrl: './voting-page.html',
  styleUrl: './voting-page.css',
})
export class VotingPage implements OnInit {
  private readonly votingCategoryApi = inject(VotingCategoryApi);
  private readonly router = inject(Router);

  public readonly categoryId = input.required<number>();

  protected categoryData = signal<VotingCategoryUserDetailDto | null>(null);

  ngOnInit(): void {
    this.fetchData();
  }

  protected fetchData() {
    this.votingCategoryApi.getCategory(this.categoryId()).subscribe({
      next: data => this.categoryData.set(data),
      error: (err: HttpErrorResponse) => {
        if (err.status === 400) {
          this.router.navigate(['/error'], {
            skipLocationChange: true,
            queryParams: {
              errorMessage: 'Kategorie nicht gefunden',
              errorDescription:
                'Diese Kategorie existiert nicht oder ist nicht mehr verfügbar.',
            },
          });
        } else {
          throw err;
        }
      },
    });
  }
}
