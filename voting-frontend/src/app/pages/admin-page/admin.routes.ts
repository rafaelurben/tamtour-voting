import { Routes } from '@angular/router';
import { UserAdminPage } from './subpages/user-admin-page/user-admin-page';
import { CategoriesAdminPage } from './subpages/categories-admin-page/categories-admin-page';
import { CategoryAdminPage } from './subpages/category-admin-page/category-admin-page';

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
    path: 'categories/:categoryId',
    component: CategoryAdminPage,
    data: { fullWidth: true },
  },
];
