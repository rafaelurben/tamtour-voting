import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UnsavedChangesService {
  public hasUnsavedChanges = signal(false);
}
