import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VotingCategoryBaseDto } from '../../dto/voting-category-base.dto';
import { VotingCandidateDto } from '../../dto/voting-candidate.dto';
import { VotingCandidateRequestDto } from '../../dto/admin/voting-candidate-request.dto';
import { VotingUserDto } from '../../dto/voting-user.dto';
import { VotingCategoryRequestDto } from '../../dto/admin/voting-category-request.dto';
import { VotingSetDto } from '../../dto/admin/voting-set.dto';
import { VotingSetUpdateDto } from '../../dto/admin/voting-set-update.dto';

@Injectable({ providedIn: 'root' })
export class AdminVotingCategoriesApi {
  private readonly http = inject(HttpClient);

  getCategories(): Observable<VotingCategoryBaseDto[]> {
    return this.http.get<VotingCategoryBaseDto[]>('/api/admin/categories');
  }

  createCategory(
    category: VotingCategoryRequestDto
  ): Observable<VotingCategoryBaseDto> {
    return this.http.post<VotingCategoryBaseDto>(
      '/api/admin/categories',
      category
    );
  }

  getCategory(categoryId: number): Observable<VotingCategoryBaseDto> {
    return this.http.get<VotingCategoryBaseDto>(
      `/api/admin/categories/${categoryId}`,
      {}
    );
  }

  updateCategory(
    categoryId: number,
    category: VotingCategoryRequestDto
  ): Observable<VotingCategoryBaseDto> {
    return this.http.put<VotingCategoryBaseDto>(
      `/api/admin/categories/${categoryId}`,
      category
    );
  }

  getCategoryResult(categoryId: number): Observable<object> {
    return this.http.get<object>(
      `/api/admin/categories/${categoryId}/result`,
      {}
    );
  }

  getSets(categoryId: number): Observable<VotingSetDto[]> {
    return this.http.get<VotingSetDto[]>(
      `/api/admin/categories/${categoryId}/sets`
    );
  }

  updateSet(
    categoryId: number,
    setId: number,
    updateDto: VotingSetUpdateDto
  ): Observable<VotingSetDto> {
    return this.http.patch<VotingSetDto>(
      `/api/admin/categories/${categoryId}/sets/${setId}`,
      updateDto
    );
  }

  getCategoryCandidates(categoryId: number): Observable<VotingCandidateDto[]> {
    return this.http.get<VotingCandidateDto[]>(
      `/api/admin/categories/${categoryId}/candidates`,
      {}
    );
  }

  addCandidate(
    categoryId: number,
    candidate: VotingCandidateRequestDto
  ): Observable<VotingUserDto> {
    return this.http.post<VotingUserDto>(
      `/api/admin/categories/${categoryId}/candidates`,
      candidate
    );
  }

  updateCandidate(
    categoryId: number,
    candidateId: number,
    candidate: VotingCandidateRequestDto
  ): Observable<VotingCandidateDto> {
    return this.http.put<VotingCandidateDto>(
      `/api/admin/categories/${categoryId}/candidates/${candidateId}`,
      candidate
    );
  }
}
