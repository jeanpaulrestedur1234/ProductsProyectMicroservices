import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductForm } from '../../components/product-form/product-form';
import { ProductsList } from '../../components/products-list/products-list';

@Component({
  selector: 'app-products-page',
  standalone: true,
  imports: [CommonModule, ProductForm, ProductsList],
  templateUrl: './products-page.html',
  styleUrls: ['./products-page.scss']
})
export class ProductsPage {
  showForm = false;

  toggleForm(): void {
    this.showForm = !this.showForm;
  }

  onProductCreated(): void {
    this.showForm = false;
  }
  onCancel(): void {
    this.showForm = false;
  }
}