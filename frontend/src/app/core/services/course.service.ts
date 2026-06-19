import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Course, Page } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/courses`;
  list(keyword = '') { let params = new HttpParams().set('size', '20'); if (keyword) params = params.set('keyword', keyword); return this.http.get<Page<Course>>(this.url, { params }); }
  create(body: Partial<Course>) { return this.http.post<Course>(this.url, body); }
  update(id: number, body: Partial<Course>) { return this.http.put<Course>(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
}
