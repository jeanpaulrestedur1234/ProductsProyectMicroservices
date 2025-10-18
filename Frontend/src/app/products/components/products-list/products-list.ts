import { Component, OnInit } from '@angular/core';
import { Products as ProductsService } from '../../services/products';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product';
@Component({
  selector: 'app-products-list',
  standalone: true,          // <- necesario para usar imports
  imports: [CommonModule],   // <- para ngIf y ngFor
  templateUrl: './products-list.html',
  styleUrls: ['./products-list.scss'] // <- corregido
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

  loadProducts(): void {
    this.loading = true;
    this.error = null;
    console.log('Loading products, page:', this.page-1, 'limit:', this.limit);

    this.productsService.listProducts(this.page-1, this.limit).subscribe({
      next: (res) => {
        console.log('Products loaded successfully:', res);

        this.products = Array.isArray(res) ? res : res.data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Error cargando productos';
        this.loading = false;
      }
    });
  }
}
