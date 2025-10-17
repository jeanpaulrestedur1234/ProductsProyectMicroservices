import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class Products {
   private apiUrl = '/products'; // Proxy Nginx

  constructor(private http: HttpClient) {}

  listProducts(page: number = 1, limit: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get(this.apiUrl, { params }).pipe(
      catchError(err => throwError(() => err))
    );
  }

  getProduct(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => err))
    );
  }
}
