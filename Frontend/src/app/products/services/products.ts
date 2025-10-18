import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class Products {
   private apiUrl = 'http://localhost:8081/product-microservice/products/'; // Proxy Nginx

  constructor(private http: HttpClient) {}

  listProducts(page: number = 0, limit: number = 10): Observable<any> {
    console.log('Fetching products from', this.apiUrl);
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<Product[]>(this.apiUrl, { params }).pipe(
      catchError(err => {
        console.error('Error fetching products:', err);
        return throwError(() => err);
      })
    );
  }

  getProduct(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => {
        console.error('Error fetching product:', err);
        return throwError(() => err);
      })
    );
  }
}
