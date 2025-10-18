import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
   private apiUrl = '/inventories'; 

  constructor(private http: HttpClient) {}

  getQuantity(productId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${productId}`).pipe(
      catchError(err => throwError(() => err))
    );
  }

  updateQuantity(productId: number, quantity: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${productId}`, { quantity }).pipe(
      catchError(err => throwError(() => err))
    );
  }
  getAllInventories(): Observable<any> {
    return this.http.get(this.apiUrl).pipe(
      catchError(err => throwError(() => err))
    );
  }

}
