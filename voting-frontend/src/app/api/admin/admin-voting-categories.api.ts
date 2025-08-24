import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VotingCategoryBaseDto } from '../../dto/voting-category-base.dto';

@Injectable({ providedIn: 'root' })
export class AdminVotingCategoriesApi {
  private readonly http = inject(HttpClient);

  getCategories(): Observable<VotingCategoryBaseDto[]> {
    return this.http.get<VotingCategoryBaseDto[]>('/api/admin/categories');
  }
}
