import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../models/product';
import { ProductsService } from '../../services/productsService';

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

  constructor(private productsService: ProductsService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  async loadProducts(): Promise<void> {
    this.loading = true;
    this.error = null;
    try {
      this.products = await this.productsService.listProducts(this.page - 1, this.limit);
    } catch (error) {
      console.error('❌ Error cargando productos:', error);
      this.error = 'Error al cargar productos.';
    } finally {
      this.loading = false;
    }
  }

  startEdit(product: Product): void {
    this.editingId = product.id!;
    this.editedProduct = { ...product };
  }

  cancelEdit(): void {
    this.editingId = null;
    this.editedProduct = null;
  }

  async saveEdit(): Promise<void> {
    if (!this.editedProduct || !this.editingId) return;
    try {
      await this.productsService.updateProduct(this.editingId.toString(), this.editedProduct);
      await this.loadProducts();
      this.cancelEdit();
    } catch (error) {
      console.error('❌ Error guardando cambios:', error);
      alert('Error al guardar cambios.');
    }
  }

  async deleteProduct(id: number): Promise<void> {
    if (confirm('¿Seguro que deseas eliminar este producto?')) {
      try {
        await this.productsService.deleteProduct(id.toString());
        this.products = this.products.filter(p => p.id !== id);
      } catch (error) {
        console.error('❌ Error eliminando producto:', error);
        alert('Error al eliminar el producto.');
      }
    }
  }
}
