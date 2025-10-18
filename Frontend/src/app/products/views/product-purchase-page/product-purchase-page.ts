// product-purchase-page.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { InventoryService } from '../../services/inventoryService';
import { ToastrService } from 'ngx-toastr';
import { ProductWithQuantity } from '../../models/product';
import { ProductDetail } from '../../components/product-detail/product-detail';

@Component({
  selector: 'app-product-purchase-page',
  standalone: true,
  imports: [CommonModule, FormsModule, ProductDetail],
  templateUrl: './product-purchase-page.html',
  styleUrl: './product-purchase-page.scss'
})
export class ProductPurchasePage implements OnInit {
  product: ProductWithQuantity | null = null;
  loading = false;
  error: string | null = null;
  quantitySelected = 1;
  availableQuantity: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private inventoryService: InventoryService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadProductData(id);
    } else {
      this.error = 'ID de producto no válido';
      this.toastr.error('No se pudo obtener el ID del producto', 'Error');
    }
  }

  async loadProductData(id: string): Promise<void> {
    this.loading = true;
    this.error = null;

    try {
      console.log(`🔍 Cargando datos del producto ID: ${id}`);
      
      const product = await this.inventoryService.getProduct(id);

      this.product = product;
      this.availableQuantity = product? product.quantity || 0 : 0;

      console.log('✅ Producto cargado:', this.product);
    } catch (error) {
      console.error('❌ Error cargando datos:', error);
      this.error = 'No se pudo cargar la información del producto';
      this.toastr.error('Error al cargar el producto', 'Error');
    } finally {
      this.loading = false;
    }
  }

  validateQuantity(): void {
    const maxQuantity = this.product?.quantity ?? 0;
    
    if (this.quantitySelected > maxQuantity) {
      this.quantitySelected = maxQuantity;
    } else if (this.quantitySelected < 1) {
      this.quantitySelected = 1;
    }
  }

  async onBuyMultiple(): Promise<void> {
    if (!this.product?.id || this.product.quantity == null) {
      this.toastr.error('Producto no disponible', 'Error');
      return;
    }

    if (this.quantitySelected > this.product.quantity) {
      this.toastr.warning('No hay suficiente stock para esta cantidad', 'Stock insuficiente');
      return;
    }

    if (this.quantitySelected < 1) {
      this.toastr.warning('Debe seleccionar al menos una unidad', 'Cantidad inválida');
      return;
    }

    try {
      const newQuantity = this.product.quantity - this.quantitySelected;
      await this.inventoryService.updateQuantity(this.product.id, newQuantity);
      
      this.product.quantity = newQuantity;
      
      console.log(`🛒 Compra de ${this.quantitySelected} unidad(es) realizada. Stock: ${newQuantity}`);
      this.toastr.success(
        `Se compraron ${this.quantitySelected} unidad(es). Stock restante: ${newQuantity}`,
        '¡Compra realizada!'
      );

      // Resetear cantidad seleccionada
      this.quantitySelected = 1;
    } catch (error) {
      console.error('❌ Error al procesar la compra:', error);
      this.toastr.error('No se pudo procesar la compra', 'Error');
    }
  }

  onBack(): void {
    this.router.navigate(['/products']);
  }

  get isQuantityValid(): boolean {
    return this.quantitySelected >= 1 && 
           this.quantitySelected <= (this.product?.quantity ?? 0);
  }

  get hasStock(): boolean {
    return (this.product?.quantity ?? 0) > 0;
  }
}