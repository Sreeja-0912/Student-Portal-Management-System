import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Attendance, Page } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class AttendanceService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/attendance`;
  record(body: { studentId: number; courseId: number; attendancePercentage: number }) { return this.http.post<Attendance>(this.url, body); }
  update(id: number, body: { studentId: number; courseId: number; attendancePercentage: number }) { return this.http.put<Attendance>(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
  byStudent(studentId: number) { return this.http.get<Attendance[]>(`${this.url}/student/${studentId}`); }
  defaulters() { return this.http.get<Page<Attendance>>(`${this.url}/defaulters?size=50`); }
}
