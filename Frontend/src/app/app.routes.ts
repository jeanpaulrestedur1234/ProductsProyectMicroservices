import { Routes } from '@angular/router';
import { ProductsPage } from './products/views/products-page/products-page';

export const routes: Routes = [
  {
    path: '',
    component: ProductsPage,
    data: { name: 'home' } // nombre de la ruta
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];
