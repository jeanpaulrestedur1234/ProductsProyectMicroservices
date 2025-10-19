import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private apiUrl = `${environment.apiUrl}/inventories`;

  private httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*' // solo si el servidor lo permite
    })
  };

  constructor(private http: HttpClient) {}

  /**
   * Obtiene la cantidad disponible de un producto por su ID
   */
  getQuantity(productId: string): Promise<number> {
    return firstValueFrom(
      this.http.get<{ quantity: number }>(`${this.apiUrl}/${productId}`, this.httpOptions)
    ).then(res => res.quantity);
  }

  /**
   * Actualiza la cantidad de un producto
   */
  updateQuantity(productId: number, quantity: number): Promise<void> {
    return firstValueFrom(
      this.http.patch<void>(`${this.apiUrl}/${productId}/purchase?quantityChange=${quantity}`, this.httpOptions)
    );
  }

  quantityPurchase(productId: number, quantity: number): Promise<void> {
    return firstValueFrom(
      this.http.post<void>(`${this.apiUrl}/${productId}/purchase?quantityToBuy=${quantity}`, this.httpOptions)
    );
  }

  /**
   * Obtiene todos los datos del inventario de un producto
   */
  getProduct(productId: string): Promise<any> {
    return firstValueFrom(
      this.http.get(`${this.apiUrl}/${productId}`, this.httpOptions)
    );
  }
}
