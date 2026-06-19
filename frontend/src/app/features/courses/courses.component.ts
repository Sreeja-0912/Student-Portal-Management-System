import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Course } from '../../core/models/api.models';
import { AuthService } from '../../core/services/auth.service';
import { CourseService } from '../../core/services/course.service';

@Component({ selector: 'sp-courses', standalone: true, imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule], template: `
<div class="page"><div class="page-header"><h1>Courses</h1></div>
  <mat-card *ngIf="auth.hasAnyRole(['ADMIN'])"><mat-card-content><form [formGroup]="form" (ngSubmit)="save()" class="form-grid">
    <mat-form-field appearance="outline"><mat-label>Code</mat-label><input matInput formControlName="courseCode"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Name</mat-label><input matInput formControlName="courseName"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Credits</mat-label><input matInput type="number" formControlName="credits"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Faculty</mat-label><input matInput formControlName="facultyName"></mat-form-field>
    <button mat-raised-button color="primary" [disabled]="form.invalid">{{editing() ? 'Update' : 'Create'}}</button>
  </form></mat-card-content></mat-card>
  <table><tr><th>Code</th><th>Name</th><th>Credits</th><th>Faculty</th><th>Actions</th></tr><tr *ngFor="let c of courses()"><td>{{c.courseCode}}</td><td>{{c.courseName}}</td><td>{{c.credits}}</td><td>{{c.facultyName}}</td><td class="actions"><button mat-button *ngIf="auth.hasAnyRole(['ADMIN'])" (click)="edit(c)">Edit</button><button mat-button color="warn" *ngIf="auth.hasAnyRole(['ADMIN'])" (click)="remove(c.id)">Delete</button></td></tr></table>
</div>` })
export class CoursesComponent implements OnInit {
  private api = inject(CourseService); private fb = inject(FormBuilder); readonly auth = inject(AuthService);
  courses = signal<Course[]>([]); editing = signal<Course | null>(null);
  form = this.fb.nonNullable.group({ courseCode: ['', Validators.required], courseName: ['', Validators.required], credits: [3, Validators.required], facultyName: [''] });
  ngOnInit(): void { this.load(); }
  load(): void { this.api.list().subscribe(p => this.courses.set(p.content)); }
  edit(c: Course): void { this.editing.set(c); this.form.patchValue(c); }
  save(): void { const value = this.form.getRawValue(); const req = this.editing() ? this.api.update(this.editing()!.id, value) : this.api.create(value); req.subscribe(() => { this.form.reset({credits: 3}); this.editing.set(null); this.load(); }); }
  remove(id: number): void { if (confirm('Delete this course?')) this.api.delete(id).subscribe(() => this.load()); }
}
