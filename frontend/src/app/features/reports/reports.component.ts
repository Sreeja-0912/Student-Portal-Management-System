import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { Attendance, BandStatistic, CoursePerformance, DepartmentPerformance, MonthlyEnrollment, PassFailStatistics, Ranking } from '../../core/models/api.models';
import { ReportService } from '../../core/services/report.service';

@Component({ selector: 'sp-reports', standalone: true, imports: [CommonModule, MatCardModule, MatTabsModule], template: `
<div class="page"><h1>Reports</h1><mat-tab-group>
  <mat-tab label="Student Rankings"><table><tr><th>Rank</th><th>Student</th><th>Average</th><th>Grade</th></tr><tr *ngFor="let r of rankings()"><td>{{r.rank}}</td><td>{{r.studentName}}</td><td>{{r.averageScore}}</td><td>{{r.grade}}</td></tr></table></mat-tab>
  <mat-tab label="Attendance Defaulters"><table><tr><th>Student</th><th>Course</th><th>%</th></tr><tr *ngFor="let r of defaulters()"><td>{{r.studentName}}</td><td>{{r.courseCode}}</td><td>{{r.attendancePercentage}}</td></tr></table></mat-tab>
  <mat-tab label="Course Performance"><table><tr><th>Course</th><th>Avg</th><th>High</th><th>Low</th></tr><tr *ngFor="let c of coursePerformance()"><td>{{c.courseCode}}</td><td>{{c.averageScore}}</td><td>{{c.highestScore}}</td><td>{{c.lowestScore}}</td></tr></table></mat-tab>
  <mat-tab label="Department"><table><tr><th>Department</th><th>Average</th></tr><tr *ngFor="let d of departmentPerformance()"><td>{{d.department}}</td><td>{{d.averageScore}}</td></tr></table></mat-tab>
  <mat-tab label="Pass/Fail"><mat-card><mat-card-title>Pass {{passFail()?.passCount ?? 0}} / Fail {{passFail()?.failCount ?? 0}}</mat-card-title><p>Pass %: {{passFail()?.passPercentage ?? 0}} | Fail %: {{passFail()?.failPercentage ?? 0}}</p></mat-card></mat-tab>
  <mat-tab label="Attendance Bands"><table><tr><th>Band</th><th>Count</th></tr><tr *ngFor="let b of bands()"><td>{{b.label}}</td><td>{{b.count}}</td></tr></table></mat-tab>
  <mat-tab label="Monthly Enrollment"><table><tr><th>Month</th><th>Enrollments</th></tr><tr *ngFor="let m of monthly()"><td>{{m.month}}</td><td>{{m.count}}</td></tr></table></mat-tab>
</mat-tab-group></div>` })
export class ReportsComponent implements OnInit { private api = inject(ReportService); rankings = signal<Ranking[]>([]); defaulters = signal<Attendance[]>([]); coursePerformance = signal<CoursePerformance[]>([]); departmentPerformance = signal<DepartmentPerformance[]>([]); passFail = signal<PassFailStatistics | null>(null); bands = signal<BandStatistic[]>([]); monthly = signal<MonthlyEnrollment[]>([]); ngOnInit(): void { const year = new Date().getFullYear(); this.api.studentRankings().subscribe(p => this.rankings.set(p.content)); this.api.attendanceDefaulters().subscribe(p => this.defaulters.set(p.content)); this.api.coursePerformance().subscribe(v => this.coursePerformance.set(v)); this.api.departmentPerformance().subscribe(v => this.departmentPerformance.set(v)); this.api.passFailStatistics().subscribe(v => this.passFail.set(v)); this.api.attendanceStatistics().subscribe(v => this.bands.set(v)); this.api.monthlyEnrollment(year).subscribe(v => this.monthly.set(v)); } }
