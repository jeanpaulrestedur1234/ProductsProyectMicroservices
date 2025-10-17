import { Routes } from '@angular/router';
import { ProductsList } from './components/products-list/products-list';
import { ProductDetail} from './components/product-detail/product-detail';

export const routes: Routes =
[
  { path: '', component: ProductsList },
  { path: 'product/:id', component: ProductDetail }
];
