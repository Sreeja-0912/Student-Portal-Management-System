import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { DashboardService } from '../../core/services/dashboard.service';
import { AtRiskStudent, Ranking, Summary, Attendance } from '../../core/models/api.models';

@Component({
  selector: 'sp-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  template: `
  <div class="page">
    <div class="page-header"><h1>Dashboard</h1></div>
    <div class="grid">
      <mat-card><mat-card-title>Total Students</mat-card-title><p class="card-value">{{ summary()?.totalStudents ?? 0 }}</p></mat-card>
      <mat-card><mat-card-title>Total Courses</mat-card-title><p class="card-value">{{ summary()?.totalCourses ?? 0 }}</p></mat-card>
      <mat-card><mat-card-title>Average Attendance</mat-card-title><p class="card-value">{{ summary()?.averageAttendance ?? 0 }}%</p></mat-card>
      <mat-card><mat-card-title>Pass Percentage</mat-card-title><p class="card-value">{{ summary()?.passPercentage ?? 0 }}%</p></mat-card>
    </div>
    <h2>Top Students</h2>
    <table><tr><th>Rank</th><th>Name</th><th>Roll No</th><th>Avg Score</th><th>Grade</th></tr><tr *ngFor="let row of topStudents()"><td>{{row.rank}}</td><td>{{row.studentName}}</td><td>{{row.rollNumber}}</td><td>{{row.averageScore}}</td><td><span class="badge">{{row.grade}}</span></td></tr></table>
    <h2>Low Attendance</h2>
    <table><tr><th>Student</th><th>Course</th><th>Attendance</th></tr><tr *ngFor="let row of lowAttendance()"><td>{{row.studentName}}</td><td>{{row.courseCode}}</td><td>{{row.attendancePercentage}}%</td></tr></table>
    <h2>At-Risk Students</h2>
    <table><tr><th>Student</th><th>Roll No</th><th>Attendance</th><th>Score</th><th>Reason</th></tr><tr *ngFor="let row of atRisk()"><td>{{row.studentName}}</td><td>{{row.rollNumber}}</td><td>{{row.averageAttendance}}%</td><td>{{row.averageScore}}</td><td>{{row.reason}}</td></tr></table>
  </div>`
})
export class DashboardComponent implements OnInit {
  private api = inject(DashboardService);
  summary = signal<Summary | null>(null);
  topStudents = signal<Ranking[]>([]);
  lowAttendance = signal<Attendance[]>([]);
  atRisk = signal<AtRiskStudent[]>([]);
  ngOnInit(): void {
    this.api.summary().subscribe(v => this.summary.set(v));
    this.api.topStudents().subscribe(v => this.topStudents.set(v));
    this.api.lowAttendance().subscribe(v => this.lowAttendance.set(v));
    this.api.atRiskStudents().subscribe(v => this.atRisk.set(v));
  }
}
