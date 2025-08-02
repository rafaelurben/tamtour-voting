import { inject } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { map } from 'rxjs';

export const adminOnlyGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const whoamiObs = authService.getWhoami();

  return whoamiObs.pipe(
    map(whoami => {
      if (whoami === null || !whoami.isAdmin) {
        return new RedirectCommand(
          router.createUrlTree(['/error'], {
            queryParams: {
              errorMessage: '403 - Zugriff verweigert',
              errorDescription:
                'Dieser Bereich ist nur für Administratoren verfügbar.',
              backUrl: '/',
            },
          }),
          {
            skipLocationChange: true,
          }
        );
      }
      return true;
    })
  );
};
