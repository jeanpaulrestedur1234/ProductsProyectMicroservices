import { Routes } from '@angular/router';
import { ProductsPage } from './products/views/products-page/products-page';
import { ProductPurchasePage } from './products/views/product-purchase-page/product-purchase-page';

export const routes: Routes = [
  {
    path: 'products',
    component: ProductsPage,
    data: { name: 'products' }
  },
  {
    path: 'products/:id',
    component: ProductPurchasePage,
    data: { name: 'productPurchase' }
  },
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'products',
    pathMatch: 'full'
  }
];