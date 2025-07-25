import { Routes } from '@angular/router';
import { LoginPage } from './pages/login-page/login-page';
import { RegisterPage } from './pages/register-page/register-page';
import { HomePage } from './pages/home-page/home-page';
import { redirectAuthenticatedGuard } from './guards/redirect-authenticated.guard';
import { authenticatedOnlyGuard } from './guards/authenticated-only.guard';
import { LogoutPage } from './pages/logout-page/logout-page';

export const routes: Routes = [
  {
    path: '',
    component: LoginPage,
    canActivate: [redirectAuthenticatedGuard],
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
    path: 'app',
    component: HomePage,
    canActivate: [authenticatedOnlyGuard],
  },
];
