import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Page, Student } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class StudentService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/students`;
  list(keyword = '') { let params = new HttpParams().set('size', '20'); if (keyword) params = params.set('keyword', keyword); return this.http.get<Page<Student>>(this.url, { params }); }
  create(body: Partial<Student>) { return this.http.post<Student>(this.url, body); }
  update(id: number, body: Partial<Student>) { return this.http.put<Student>(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
  me() { return this.http.get<Student>(`${this.url}/me`); }
}
