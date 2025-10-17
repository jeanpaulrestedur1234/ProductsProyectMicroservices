import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Products as ProductsService } from '../../services/products';
import { Inventory as InventoryService } from '../../services/inventory';
@Component({
  selector: 'app-product-detail',
  imports: [],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.scss'
})
export class ProductDetail {

   product: any = null;
  quantity: number | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productsService: ProductsService,
    private inventoryService: InventoryService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.loadProduct(id);
  }

  loadProduct(id: string): void {
    this.loading = true;
    this.error = null;
    this.productsService.getProduct(id).subscribe({
      next: (res) => {
        this.product = res.data;
        this.loadInventory(id);
      },
      error: () => {
        this.error = 'Producto no encontrado';
        this.loading = false;
      }
    });
  }

  loadInventory(id: string): void {
    this.inventoryService.getQuantity(id).subscribe({
      next: (res) => {
        this.quantity = res.data.attributes.quantity;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error cargando inventario';
        this.loading = false;
      }
    });
  }

  buy(): void {
    if (!this.product || !this.quantity) return;
    if (this.quantity > 0) {
      const id = this.product.id;
      this.inventoryService.updateQuantity(id, this.quantity - 1).subscribe({
        next: () => {
          this.quantity = this.quantity! - 1;
        },
        error: () => {
          this.error = 'Error al actualizar inventario';
        }
      });
    } else {
      alert('No hay stock disponible');
    }
  }
}
