import { inject } from '@angular/core';
import { AuthService } from '../api/auth.service';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { map } from 'rxjs';

export const authenticatedOnlyGuard: CanActivateFn = route => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const whoamiObs = authService.getWhoami();

  return whoamiObs.pipe(
    map(whoami => {
      if (whoami === null) {
        const loginPath = router.parseUrl('/');
        return new RedirectCommand(loginPath, {
          skipLocationChange: false,
        });
      }
      if (
        !whoami.user.initialRegistrationComplete &&
        !route.routeConfig?.path?.includes('register') &&
        !route.routeConfig?.path?.includes('logout')
      ) {
        const registerPath = router.parseUrl('/register');
        return new RedirectCommand(registerPath, {
          skipLocationChange: false,
        });
      }
      return true;
    })
  );
};
