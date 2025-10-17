import { Component } from '@angular/core';
import { Products as ProductsService } from '../../services/products';
@Component({
  selector: 'app-products-list',
  imports: [],
  templateUrl: './products-list.html',
  styleUrl: './products-list.scss'
})
export class ProductsList {

  products: any[] = [];
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
    this.productsService.listProducts(this.page, this.limit).subscribe({
      next: (res) => {
        this.products = res.data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error cargando productos';
        this.loading = false;
      }
    });
  }
}
