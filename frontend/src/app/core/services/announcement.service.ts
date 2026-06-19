import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Announcement, Page } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class AnnouncementService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/announcements`;
  list() { return this.http.get<Page<Announcement>>(`${this.url}?size=20`); }
  create(body: { title: string; description: string }) { return this.http.post<Announcement>(this.url, body); }
  update(id: number, body: { title: string; description: string }) { return this.http.put<Announcement>(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
}
