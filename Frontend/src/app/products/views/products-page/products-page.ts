import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductsList } from '../../components/products-list/products-list';
import { ProductForm } from '../../components/product-form/product-form';

@Component({
  selector: 'app-products-page',
  standalone: true,
  imports: [CommonModule, ProductsList, ProductForm],
  templateUrl: './products-page.html',
  styleUrl: './products-page.scss'
})
export class ProductsPage {
   showForm = false;

  toggleForm(): void {
    this.showForm = !this.showForm;
  }

}
