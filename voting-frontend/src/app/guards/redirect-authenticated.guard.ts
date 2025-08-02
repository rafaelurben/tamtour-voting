import { inject } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { map } from 'rxjs';

export const redirectAuthenticatedGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const whoamiObs = authService.getWhoami();

  return whoamiObs.pipe(
    map(whoami => {
      if (whoami !== null && whoami.user) {
        const appPath = router.parseUrl('/');
        return new RedirectCommand(appPath, {
          skipLocationChange: false,
        });
      }
      return true;
    })
  );
};
