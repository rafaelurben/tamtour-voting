import { Routes } from '@angular/router';
import { LoginPage } from './pages/login-page/login-page';
import { RegisterPage } from './pages/register-page/register-page';
import { HomePage } from './pages/home-page/home-page';
import { redirectAuthenticatedGuard } from './guards/redirect-authenticated.guard';
import { authenticatedOnlyGuard } from './guards/authenticated-only.guard';
import { LogoutPage } from './pages/logout-page/logout-page';
import { VotingPage } from './pages/voting-page/voting-page';
import { RulesPage } from './pages/rules-page/rules-page';
import { ErrorPage } from './pages/error-page/error-page';
import { ProfilePage } from './pages/profile-page/profile-page';
import { adminOnlyGuard } from './guards/admin-only.guard';
import { unsavedChangesGuard } from './guards/unsaved-changes.guard';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginPage,
    canActivate: [redirectAuthenticatedGuard],
    data: { hideHeader: true },
  },
  {
    path: 'logout',
    component: LogoutPage,
    canActivate: [authenticatedOnlyGuard],
  },
  {
    path: 'register',
    component: RegisterPage,
    canActivate: [authenticatedOnlyGuard],
  },
  {
    path: 'profile',
    component: ProfilePage,
    canActivate: [authenticatedOnlyGuard],
  },
  {
    path: '',
    component: HomePage,
    canActivate: [authenticatedOnlyGuard],
  },
  {
    path: 'vote/:categoryId',
    component: VotingPage,
    canActivate: [authenticatedOnlyGuard],
    canDeactivate: [unsavedChangesGuard],
  },
  {
    path: 'rules',
    component: RulesPage,
  },
  {
    path: 'error',
    component: ErrorPage,
  },
  {
    path: 'admin',
    canActivate: [adminOnlyGuard],
    canActivateChild: [adminOnlyGuard],
    loadComponent: () =>
      import('./pages/admin-page/admin-page').then(m => m.AdminPage),
    loadChildren: () =>
      import('./pages/admin-page/admin.routes').then(m => m.routes),
    data: {
      fullWidth: true,
    },
  },
  {
    path: '**',
    component: ErrorPage,
    data: {
      errorMessage: '404 - Seite nicht gefunden',
      errorDescription:
        'Die angeforderte Seite existiert nicht oder wurde verschoben.',
    },
  },
];
