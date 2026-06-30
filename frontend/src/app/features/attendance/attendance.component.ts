import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { Attendance } from '../../core/models/api.models';
import { AttendanceService } from '../../core/services/attendance.service';
import { AuthService } from '../../core/services/auth.service';
import { StudentService } from '../../core/services/student.service';

@Component({
  selector: 'sp-attendance',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatCardModule,
    MatFormFieldModule, MatInputModule, MatTabsModule],
  template: `
    <div class="page">
      <h1>Attendance</h1>

      <!-- STUDENT VIEW — see only their own attendance -->
      <ng-container *ngIf="isStudent()">
        <mat-card>
          <mat-card-content>
            <table>
              <tr><th>Course</th><th>Code</th><th>Attendance %</th><th>Status</th></tr>
              <tr *ngFor="let row of myAttendance()">
                <td>{{ row.courseName }}</td>
                <td>{{ row.courseCode }}</td>
                <td>{{ row.attendancePercentage }}%</td>
                <td>
                  <span [style.color]="row.attendancePercentage >= 75 ? 'green' : 'red'">
                    {{ row.attendancePercentage >= 75 ? 'Regular' : 'Defaulter' }}
                  </span>
                </td>
              </tr>
              <tr *ngIf="myAttendance().length === 0">
                <td colspan="4">No attendance records found.</td>
              </tr>
            </table>
          </mat-card-content>
        </mat-card>
      </ng-container>

      <!-- ADMIN / FACULTY VIEW -->
      <ng-container *ngIf="!isStudent()">
        <mat-tab-group>
          <mat-tab label="Record Attendance">
            <mat-card><mat-card-content>
              <form [formGroup]="form" (ngSubmit)="save()" class="form-grid">
                <mat-form-field appearance="outline">
                  <mat-label>Student ID</mat-label>
                  <input matInput type="number" formControlName="studentId">
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Course ID</mat-label>
                  <input matInput type="number" formControlName="courseId">
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Attendance %</mat-label>
                  <input matInput type="number" formControlName="attendancePercentage">
                </mat-form-field>
                <button mat-raised-button color="primary" [disabled]="form.invalid">Record</button>
              </form>
            </mat-card-content></mat-card>
          </mat-tab>
          <mat-tab label="Defaulters">
            <table>
              <tr><th>Student</th><th>Course</th><th>Attendance %</th></tr>
              <tr *ngFor="let row of defaulters()">
                <td>{{ row.studentName }}</td>
                <td>{{ row.courseCode }}</td>
                <td><span class="badge">{{ row.attendancePercentage }}%</span></td>
              </tr>
            </table>
          </mat-tab>
        </mat-tab-group>
      </ng-container>
    </div>
  `
})
export class AttendanceComponent implements OnInit {
  private api = inject(AttendanceService);
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private studentService = inject(StudentService);

  defaulters = signal<Attendance[]>([]);
  myAttendance = signal<Attendance[]>([]);

  isStudent() {
    return this.auth.hasAnyRole(['STUDENT']);
  }

  form = this.fb.nonNullable.group({
    studentId: [1, Validators.required],
    courseId: [1, Validators.required],
    attendancePercentage: [75, Validators.required]
  });

  ngOnInit(): void {
    if (this.isStudent()) {
      // Get student's own ID via /api/students/me, then load their attendance
      this.studentService.me().subscribe(student => {
        this.api.byStudent(student.id).subscribe(data => {
          this.myAttendance.set(data);
        });
      });
    } else {
      this.load();
    }
  }

  load(): void {
    this.api.defaulters().subscribe(p => this.defaulters.set(p.content));
  }

  save(): void {
    this.api.record(this.form.getRawValue()).subscribe(() => {
      this.form.reset({ studentId: 1, courseId: 1, attendancePercentage: 75 });
      this.load();
    });
  }
}