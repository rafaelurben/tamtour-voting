import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResultViewerKeyDto } from '../../dto/admin/result-viewer-key.dto';
import { ResultViewerKeyRequestDto } from '../../dto/admin/result-viewer-key-request.dto';

@Injectable({ providedIn: 'root' })
export class AdminResultViewerKeysApi {
  private readonly http = inject(HttpClient);

  getKeys(): Observable<ResultViewerKeyDto[]> {
    return this.http.get<ResultViewerKeyDto[]>('/api/admin/viewer/keys');
  }

  createKey(dto: ResultViewerKeyRequestDto): Observable<ResultViewerKeyDto> {
    return this.http.post<ResultViewerKeyDto>('/api/admin/viewer/keys', dto);
  }

  updateKey(
    id: number,
    dto: ResultViewerKeyRequestDto
  ): Observable<ResultViewerKeyDto> {
    return this.http.put<ResultViewerKeyDto>(
      `/api/admin/viewer/keys/${id}`,
      dto
    );
  }

  deleteKey(id: number): Observable<void> {
    return this.http.delete<void>(`/api/admin/viewer/keys/${id}`);
  }
}
