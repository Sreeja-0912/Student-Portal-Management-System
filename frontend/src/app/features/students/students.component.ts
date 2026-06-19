import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Student } from '../../core/models/api.models';
import { AuthService } from '../../core/services/auth.service';
import { StudentService } from '../../core/services/student.service';

@Component({ selector: 'sp-students', standalone: true, imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule], template: `
<div class="page">
  <div class="page-header"><h1>Students</h1><button mat-raised-button color="primary" *ngIf="auth.hasAnyRole(['ADMIN'])" (click)="editing.set(null); form.reset()">Add Student</button></div>
  <mat-card *ngIf="auth.hasAnyRole(['ADMIN'])"><mat-card-content><form [formGroup]="form" (ngSubmit)="save()" class="form-grid">
    <mat-form-field appearance="outline"><mat-label>Roll No</mat-label><input matInput formControlName="rollNumber"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>First Name</mat-label><input matInput formControlName="firstName"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Last Name</mat-label><input matInput formControlName="lastName"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Email</mat-label><input matInput formControlName="email"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Department</mat-label><input matInput formControlName="department"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Semester</mat-label><input matInput type="number" formControlName="semester"></mat-form-field>
    <button mat-raised-button color="primary" [disabled]="form.invalid">{{ editing() ? 'Update' : 'Create' }}</button>
  </form></mat-card-content></mat-card>
  <table><tr><th>Roll No</th><th>Name</th><th>Email</th><th>Department</th><th>Semester</th><th>Actions</th></tr><tr *ngFor="let s of students()"><td>{{s.rollNumber}}</td><td>{{s.firstName}} {{s.lastName}}</td><td>{{s.email}}</td><td>{{s.department}}</td><td>{{s.semester}}</td><td class="actions"><button mat-button (click)="edit(s)" *ngIf="auth.hasAnyRole(['ADMIN'])">Edit</button><button mat-button color="warn" (click)="remove(s.id)" *ngIf="auth.hasAnyRole(['ADMIN'])">Delete</button></td></tr></table>
</div>` })
export class StudentsComponent implements OnInit {
  private api = inject(StudentService); private fb = inject(FormBuilder); readonly auth = inject(AuthService);
  students = signal<Student[]>([]); editing = signal<Student | null>(null);
  form = this.fb.nonNullable.group({ rollNumber: ['', Validators.required], firstName: ['', Validators.required], lastName: [''], email: ['', [Validators.required, Validators.email]], phone: [''], department: [''], semester: [1] });
  ngOnInit(): void { this.load(); }
  load(): void { this.api.list().subscribe(p => this.students.set(p.content)); }
  edit(s: Student): void { this.editing.set(s); this.form.patchValue(s as any); }
  save(): void { const value = this.form.getRawValue(); const req = this.editing() ? this.api.update(this.editing()!.id, value) : this.api.create(value); req.subscribe(() => { this.form.reset(); this.editing.set(null); this.load(); }); }
  remove(id: number): void { if (confirm('Delete this student?')) this.api.delete(id).subscribe(() => this.load()); }
}
