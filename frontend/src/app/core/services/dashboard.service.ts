import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AtRiskStudent, BandStatistic, Ranking, Summary, Attendance } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/dashboard`;
  summary() { return this.http.get<Summary>(`${this.url}/summary`); }
  topStudents() { return this.http.get<Ranking[]>(`${this.url}/top-students?limit=5`); }
  lowAttendance() { return this.http.get<Attendance[]>(`${this.url}/low-attendance`); }
  atRiskStudents() { return this.http.get<AtRiskStudent[]>(`${this.url}/at-risk-students`); }
}
