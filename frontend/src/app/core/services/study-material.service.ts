import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Page, StudyMaterial } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class StudyMaterialService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/study-materials`;
  list() { return this.http.get<Page<StudyMaterial>>(`${this.url}?size=50`); }
  byCourse(courseId: number) { return this.http.get<StudyMaterial[]>(`${this.url}/course/${courseId}`); }
  create(body: { courseId: number; title: string; fileUrl: string }) { return this.http.post<StudyMaterial>(this.url, body); }
  update(id: number, body: { courseId: number; title: string; fileUrl: string }) { return this.http.put<StudyMaterial>(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
}
