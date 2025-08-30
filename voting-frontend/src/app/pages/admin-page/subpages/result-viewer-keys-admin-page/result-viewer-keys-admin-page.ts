import { Component, inject } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Spinner } from '../../../../components/spinner/spinner';
import { AsyncPipe } from '@angular/common';
import { Button } from '../../../../components/button/button';
import { AdminResultViewerKeysApi } from '../../../../api/admin/admin-result-viewer-keys.api';
import { ResultViewerKeyDto } from '../../../../dto/admin/result-viewer-key.dto';
import { Alert } from '../../../../components/alert/alert';

@Component({
  selector: 'app-categories-admin-page',
  imports: [Spinner, AsyncPipe, Button, Alert],
  templateUrl: './result-viewer-keys-admin-page.html',
  styleUrl: './result-viewer-keys-admin-page.css',
})
export class ResultViewerKeysAdminPage {
  protected keys$!: Observable<ResultViewerKeyDto[]>;

  private readonly viewerKeysApi = inject(AdminResultViewerKeysApi);

  private loadKeys() {
    this.keys$ = this.viewerKeysApi
      .getKeys()
      .pipe(map(c => c.toSorted((a, b) => a.id - b.id)));
  }

  constructor() {
    this.loadKeys();
  }

  protected createKey() {
    const name = prompt('Name des neuen Schlüssels:');
    if (!name) {
      return;
    }
    this.viewerKeysApi.createKey({ name }).subscribe({
      next: () => {
        this.loadKeys();
      },
      error: error => {
        throw error;
      },
    });
  }

  protected updateKey(key: ResultViewerKeyDto) {
    const name = prompt('Name des Schlüssels:', key.name);
    if (!name) {
      return;
    }
    this.viewerKeysApi.updateKey(key.id, { name }).subscribe({
      next: () => {
        this.loadKeys();
      },
      error: error => {
        throw error;
      },
    });
  }

  protected deleteKey(key: ResultViewerKeyDto) {
    if (!confirm(`Schlüssel "${key.name}" wirklich löschen?`)) {
      return;
    }
    this.viewerKeysApi.deleteKey(key.id).subscribe({
      next: () => {
        this.loadKeys();
      },
      error: error => {
        throw error;
      },
    });
  }
}
