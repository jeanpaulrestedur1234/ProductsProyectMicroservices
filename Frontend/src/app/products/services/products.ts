import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';
import { Product } from '../models/product';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class Products {
  private api: AxiosInstance;

  constructor() {
    // Configura instancia Axios con la URL base global
    this.api = axios.create({
      baseURL: environment.apiUrl,
      headers: {
        'Content-Type': 'application/json'
      },
      timeout: 10000
    });
  }

  // 🔹 Obtener lista de productos
  async listProducts(page: number = 0, limit: number = 10): Promise<Product[]> {
    try {
      const response = await this.api.get('/products', { params: { page, limit } });
      return response.data;
    } catch (error) {
      console.error('❌ Error al obtener productos:', error);
      throw error;
    }
  }

  // 🔹 Obtener producto por ID
  async getProduct(id: string): Promise<Product> {
    try {
      const response = await this.api.get(`/products/${id}`);
      return response.data;
    } catch (error) {
      console.error(`❌ Error al obtener producto con ID ${id}:`, error);
      throw error;
    }
  }

  // 🔹 Crear producto
  async createProduct(product: Product): Promise<Product> {
    try {
      const response = await this.api.post('/products/', product);
      return response.data;
    } catch (error) {
      console.error('❌ Error al crear producto:', error);
      throw error;
    }
  }

  // 🔹 Actualizar producto
  async updateProduct(id: string, product: Product): Promise<Product> {
    try {
      const response = await this.api.put(`/products/${id}`, product);
      return response.data;
    } catch (error) {
      console.error(`❌ Error al actualizar producto ${id}:`, error);
      throw error;
    }
  }

  // 🔹 Eliminar producto
  async deleteProduct(id: string): Promise<void> {
    try {
      await this.api.delete(`/products//${id}`);
    } catch (error) {
      console.error(`❌ Error al eliminar producto ${id}:`, error);
      throw error;
    }
  }
}
