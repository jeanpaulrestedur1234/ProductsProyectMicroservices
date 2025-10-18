import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import {  ProductsService } from '../../services/productsService';
import {  InventoryService } from '../../services/inventoryService';
import { Product } from '../../models/product';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-detail.html',
  styleUrls: ['./product-detail.scss']
})
export class ProductDetail implements OnInit {

  product: Product | null = null;
  quantity: number | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productsService: ProductsService,
    private inventoryService: InventoryService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadProduct(id);
    } else {
      this.error = 'Producto no encontrado';
    }
  }

  async loadProduct(id: string): Promise<void> {
    this.loading = true;
    this.error = null;

    try {
      console.log(`🔍 Cargando producto con ID: ${id}`);
      this.product = await this.productsService.getProduct(id);
      console.log('✅ Producto cargado:', this.product);
      await this.loadInventory(id);
    } catch (error) {
      console.error('❌ Error al cargar producto:', error);
      this.error = 'No se pudo cargar el producto.';
    } finally {
      this.loading = false;
    }
  }

  async loadInventory(id: string): Promise<void> {
    try {
      const res = await this.inventoryService.getQuantity(id);

      console.log('📦 Inventario cargado:', this.quantity);
    } catch (error) {
      console.error('❌ Error cargando inventario:', error);
      this.error = 'Error al cargar inventario.';
    }
  }

  async buy(): Promise<void> {
    if (!this.product || this.quantity == null) return;

    if (this.quantity > 0) {
      try {
        const newQuantity = this.quantity - 1;
        await this.inventoryService.updateQuantity(this.product.id, newQuantity);
        this.quantity = newQuantity;
        console.log(`🛒 Compra realizada. Stock actualizado a ${this.quantity}`);
      } catch (error) {
        console.error('❌ Error al actualizar inventario:', error);
        this.error = 'No se pudo actualizar el inventario.';
      }
    } else {
      alert('⚠️ No hay stock disponible');
    }
  }
}
