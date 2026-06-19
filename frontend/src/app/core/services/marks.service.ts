import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Marks, Page, Ranking } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class MarksService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/marks`;
  record(body: { studentId: number; courseId: number; score: number }) { return this.http.post<Marks>(this.url, body); }
  update(id: number, body: { studentId: number; courseId: number; score: number }) { return this.http.put<Marks>(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
  byStudent(studentId: number) { return this.http.get<Marks[]>(`${this.url}/student/${studentId}`); }
  rankings() { return this.http.get<Page<Ranking>>(`${this.url}/rankings?size=50`); }
}
