import { CanDeactivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { UnsavedChangesService } from '../service/unsaved-changes.service';

export const unsavedChangesGuard: CanDeactivateFn<unknown> = () => {
  const unsavedChangesService = inject(UnsavedChangesService);

  if (unsavedChangesService.hasUnsavedChanges()) {
    const confirmLeave = window.confirm(
      'Du hast ungespeicherte Änderungen. Möchtest du die Seite wirklich verlassen?'
    );
    if (confirmLeave) {
      unsavedChangesService.hasUnsavedChanges.set(false);
    } else {
      return false;
    }
  }

  return true;
};
