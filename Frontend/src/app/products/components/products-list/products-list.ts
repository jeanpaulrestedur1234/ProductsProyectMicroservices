import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../models/product';
import { ProductsService } from '../../services/productsService';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-products-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products-list.html',
  styleUrls: ['./products-list.scss'],
})
export class ProductsList implements OnInit {
  products: Product[] = [];
  loading = false;
  error: string | null = null;
  editingId: number | null = null;
  editedProduct: Product | null = null;
  page = 1;
  limit = 10;
  availableNext = true;
  limitOptions = [5, 10, 20, 50];

  constructor(
    private productsService: ProductsService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  async loadProducts(): Promise<void> {
    this.loading = true;
    this.error = null;

    try {
      const newProducts: Product[] = await this.productsService.listProducts(this.page - 1, this.limit);

      if (!newProducts || newProducts.length === 0) {
        this.availableNext = false;
        if (this.page > 1) {
          this.page--;
          this.toastr.warning('No hay más productos para mostrar.', 'Aviso');
        } else {
          this.products = [];
          this.toastr.info('No se encontraron productos.', 'Información');
        }
        return;
      }

      this.products = newProducts;
      this.availableNext = newProducts.length == this.limit;
    } catch (error: unknown) {
      this.error = 'Error al cargar productos.';
      this.toastr.error(this.error, 'Error');
    } finally {
      this.loading = false;
    }
  }

  onLimitChange(): void {
    this.page = 1;
    this.loadProducts();
  }

  startEdit(product: Product): void {
    this.editingId = product.id!;
    this.editedProduct = JSON.parse(JSON.stringify(product));
  }
  goToProductDetail(productId: number) {
    this.router.navigate(['/products/purchase'], { queryParams: { id: productId } });
  }


  cancelEdit(): void {
    this.editingId = null;
    this.editedProduct = null;
  }

  reload(): void {
    this.page = 1;
    this.loadProducts();
  }

  trackById(index: number, product: Product): number {
    return product.id!;
  }

  prevPage(): void {
    if (this.page > 1) {
      this.page--;
      this.loadProducts();
    }
  }

  nextPage(): void {
    if (this.availableNext) {
      this.page++;
      this.loadProducts();
    } else {
      this.toastr.warning('No hay más productos para mostrar.', 'Aviso');
    }
  }

  canPrev(): boolean {
    return this.page > 1;
  }

  canNext(): boolean {
    return this.availableNext;
  }

  async saveEdit(): Promise<void> {
    if (!this.editedProduct || !this.editingId) return;

    try {
      await this.productsService.updateProduct(this.editingId.toString(), this.editedProduct);
      await this.loadProducts();
      this.cancelEdit();
      this.toastr.success('Producto actualizado correctamente.', 'Éxito');
    } catch (error: unknown) {
      this.toastr.error('Error al guardar cambios.', 'Error');
    }
  }

  async deleteProduct(id: number): Promise<void> {
    if (!confirm('¿Seguro que deseas eliminar este producto?')) return;

    try {
      await this.productsService.deleteProduct(id.toString());
      this.toastr.success('Producto eliminado correctamente.', 'Éxito');

      if (this.products.length === 1 && this.page > 1) {
        this.page--;
      }
      await this.loadProducts();
    } catch (error: unknown) {
      this.toastr.error('Error al eliminar el producto.', 'Error');
    }
  }
}
