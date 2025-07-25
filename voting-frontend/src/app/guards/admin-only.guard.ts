import { inject } from '@angular/core';
import { AuthService } from '../api/auth.service';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { map } from 'rxjs';

export const adminOnlyGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const whoamiObs = authService.getWhoami();

  return whoamiObs.pipe(
    map(whoami => {
      if (whoami === null || !whoami.isAdmin) {
        const path403 = router.parseUrl('/403');
        return new RedirectCommand(path403, {
          skipLocationChange: true,
        });
      }
      return true;
    })
  );
};
