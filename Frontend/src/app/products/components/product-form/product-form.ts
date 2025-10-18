import { Component, inject, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Product } from '../../models/product';
import { ProductsService } from '../../services/productsService';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.html',
  styleUrls: ['./product-form.scss']
})
export class ProductForm {
  @Output() productCreated = new EventEmitter<void>();
  
  productForm: FormGroup;
  private productsService = inject(ProductsService);
  private toastr = inject(ToastrService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  constructor() {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      sku: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      description: ['']
    });
  }

  async onSubmit(): Promise<void> {
    if (this.productForm.invalid) {
      this.toastr.warning('Revisa los campos del formulario', 'Aviso');
      this.markFormGroupTouched(this.productForm);
      return;
    }

    const newProduct: Product = this.productForm.value as Product;

    try {
      const created = await this.productsService.createProduct(newProduct);
      this.toastr.success(`Producto "${created.name}" creado correctamente`, 'Éxito');
      this.productCreated.emit();
    } catch (error) {
      console.error('❌ Error creando producto:', error);
      this.toastr.error('Error al crear el producto', 'Error');
    }
  }

  onCancel(): void {
    this.productCreated.emit();
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}