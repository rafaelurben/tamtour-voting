import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VotingCategoryUserOverviewDto } from '../dto/voting-category-user-overview.dto';
import { VotingCategoryUserDetailDto } from '../dto/voting-category-user-detail.dto';
import { VotingPositionMapDto } from '../dto/voting-position-map.dto';

@Injectable({ providedIn: 'root' })
export class AuthApi {
  private http = inject(HttpClient);

  getCategories(): Observable<VotingCategoryUserOverviewDto[]> {
    return this.http.get<VotingCategoryUserOverviewDto[]>('/api/categories');
  }

  getCategory(categoryId: number): Observable<VotingCategoryUserDetailDto> {
    return this.http.get<VotingCategoryUserDetailDto>(
      `/api/categories/${categoryId}`
    );
  }

  updateCategoryVotingPositions(
    categoryId: number,
    votingPositions: VotingPositionMapDto
  ): Observable<void> {
    return this.http.put<void>(
      `/api/categories/${categoryId}/positions`,
      votingPositions
    );
  }

  submitCategoryVoting(categoryId: number): Observable<void> {
    return this.http.post<void>(`/api/categories/${categoryId}/submit`, {});
  }
}
