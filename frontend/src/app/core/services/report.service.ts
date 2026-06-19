import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Attendance, BandStatistic, CoursePerformance, DepartmentPerformance, MonthlyEnrollment, Page, PassFailStatistics, Ranking } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/reports`;
  studentRankings() { return this.http.get<Page<Ranking>>(`${this.url}/student-rankings?size=50`); }
  attendanceDefaulters() { return this.http.get<Page<Attendance>>(`${this.url}/attendance-defaulters?size=50`); }
  coursePerformance() { return this.http.get<CoursePerformance[]>(`${this.url}/course-performance`); }
  departmentPerformance() { return this.http.get<DepartmentPerformance[]>(`${this.url}/department-performance`); }
  passFailStatistics() { return this.http.get<PassFailStatistics>(`${this.url}/pass-fail-statistics`); }
  attendanceStatistics() { return this.http.get<BandStatistic[]>(`${this.url}/attendance-statistics`); }
  monthlyEnrollment(year: number) { return this.http.get<MonthlyEnrollment[]>(`${this.url}/monthly-enrollment?year=${year}`); }
}
