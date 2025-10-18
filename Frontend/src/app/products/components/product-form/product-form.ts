import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Product } from '../../models/product';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.html',
  styleUrls: ['./product-form.scss']
})
export class ProductForm {
  productForm!: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      sku: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      description: ['']
    });
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const newProduct: Product = this.productForm.value as Product;
      console.log('✅ Producto creado:', newProduct);
      alert(`✅ Producto "${newProduct.name}" creado exitosamente`);
      this.router.navigate(['/products']);
    }
  }

  onCancel(): void {
    this.router.navigate(['/products']);
  }
}
