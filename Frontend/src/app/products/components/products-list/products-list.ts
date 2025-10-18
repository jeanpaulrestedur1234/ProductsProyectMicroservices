import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product';
import {Products as ProductsService} from '../../services/products';
@Component({
  selector: 'app-products-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './products-list.html',
  styleUrls: ['./products-list.scss']
})
export class ProductsList implements OnInit {
  products: Product[] = [];
  loading = false;
  error: string | null = null;
  page = 1;
  limit = 10;

  constructor(private productsService: ProductsService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  async loadProducts(): Promise<void> {
    this.loading = true;
    this.error = null;

    try {
      console.log(`🔄 Loading products — page: ${this.page - 1}, limit: ${this.limit}`);
      const response = await this.productsService.listProducts(this.page - 1, this.limit);
      this.products = Array.isArray(response) ? response : [];
      console.log('✅ Products loaded successfully:', this.products);
    } catch (error) {
      console.error('❌ Error loading products:', error);
      this.error = 'Error cargando productos. Intenta nuevamente.';
    } finally {
      this.loading = false;
    }
  }
}
