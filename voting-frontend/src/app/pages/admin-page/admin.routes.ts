import { Routes } from '@angular/router';
import { UserAdminPage } from './subpages/user-admin-page/user-admin-page';
import { CategoriesAdminPage } from './subpages/categories-admin-page/categories-admin-page';
import { CategoryAdminEditPage } from './subpages/category-admin-edit-page/category-admin-edit-page';
import { ErrorPage } from '../error-page/error-page';
import { CategoryAdminVotesPage } from './subpages/category-admin-votes-page/category-admin-votes-page';
import { ResultViewerKeysAdminPage } from './subpages/result-viewer-keys-admin-page/result-viewer-keys-admin-page';

export const routes: Routes = [
  {
    path: 'users',
    component: UserAdminPage,
    data: { fullWidth: true },
  },
  {
    path: 'categories',
    component: CategoriesAdminPage,
    data: { fullWidth: true },
  },
  {
    path: 'categories/:categoryId/edit',
    component: CategoryAdminEditPage,
    data: { fullWidth: true },
  },
  {
    path: 'categories/:categoryId/votes',
    component: CategoryAdminVotesPage,
    data: { fullWidth: true },
  },
  {
    path: 'viewer-keys',
    component: ResultViewerKeysAdminPage,
    data: { fullWidth: true },
  },
  {
    path: '**',
    component: ErrorPage,
    data: {
      errorMessage: '404 - Seite nicht gefunden',
      errorDescription:
        'Die angeforderte Admin-Seite existiert nicht oder wurde verschoben.',
      fullWidth: true,
    },
  },
];
