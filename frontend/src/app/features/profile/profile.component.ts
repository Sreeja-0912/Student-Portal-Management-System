import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../../core/services/auth.service';
import { StudentService } from '../../core/services/student.service';
import { Student } from '../../core/models/api.models';

@Component({ selector: 'sp-profile', standalone: true, imports: [CommonModule, MatCardModule], template: `
<div class="page"><h1>Profile</h1><div class="grid"><mat-card><mat-card-title>Account Info</mat-card-title><p><strong>Username:</strong> {{auth.user()?.username}}</p><p><strong>Email:</strong> {{auth.user()?.email}}</p><p><strong>Role:</strong> <span class="badge">{{auth.user()?.role}}</span></p></mat-card><mat-card *ngIf="profile()"><mat-card-title>Student Academic Info</mat-card-title><p><strong>Roll No:</strong> {{profile()?.rollNumber}}</p><p><strong>Department:</strong> {{profile()?.department}}</p><p><strong>Semester:</strong> {{profile()?.semester}}</p></mat-card><mat-card><mat-card-title>Permissions</mat-card-title><p *ngIf="auth.user()?.role === 'ADMIN'">Full access to students, courses, reports, study materials and audit logs.</p><p *ngIf="auth.user()?.role === 'FACULTY'">Manage attendance, marks, reports and announcements.</p><p *ngIf="auth.user()?.role === 'STUDENT'">View own profile, courses, attendance, marks and materials.</p></mat-card></div></div>` })
export class ProfileComponent implements OnInit { readonly auth = inject(AuthService); private students = inject(StudentService); profile = signal<Student | null>(null); ngOnInit(): void { if (this.auth.user()?.role === 'STUDENT') this.students.me().subscribe(v => this.profile.set(v)); } }
