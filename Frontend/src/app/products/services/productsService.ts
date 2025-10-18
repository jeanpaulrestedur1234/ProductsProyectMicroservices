import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Product } from '../models/product';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  listProducts(page = 0, limit = 10): Promise<Product[]> {
    const params = new HttpParams().set('page', page).set('limit', limit);
    return firstValueFrom(this.http.get<Product[]>(`${this.baseUrl}/products`, { params }));
  }

  getProduct(id: string): Promise<Product> {
    return firstValueFrom(this.http.get<Product>(`${this.baseUrl}/products/${id}`));
  }

  createProduct(product: Product): Promise<Product> {
    return firstValueFrom(this.http.post<Product>(`${this.baseUrl}/products`, product));
  }

  updateProduct(id: string, product: Product): Promise<Product> {
    return firstValueFrom(this.http.put<Product>(`${this.baseUrl}/products/${id}`, product));
  }

  deleteProduct(id: string): Promise<void> {
    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/products/${id}`));
  }
}
