import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { Ranking } from '../../core/models/api.models';
import { MarksService } from '../../core/services/marks.service';

@Component({ selector: 'sp-marks', standalone: true, imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatTabsModule], template: `
<div class="page"><h1>Marks</h1><mat-tab-group><mat-tab label="Enter Marks"><mat-card><mat-card-content><form [formGroup]="form" (ngSubmit)="save()" class="form-grid"><mat-form-field appearance="outline"><mat-label>Student ID</mat-label><input matInput type="number" formControlName="studentId"></mat-form-field><mat-form-field appearance="outline"><mat-label>Course ID</mat-label><input matInput type="number" formControlName="courseId"></mat-form-field><mat-form-field appearance="outline"><mat-label>Score</mat-label><input matInput type="number" formControlName="score"></mat-form-field><button mat-raised-button color="primary" [disabled]="form.invalid">Save</button></form></mat-card-content></mat-card></mat-tab><mat-tab label="Rankings"><table><tr><th>Rank</th><th>Student</th><th>Roll No</th><th>Average</th><th>Grade</th></tr><tr *ngFor="let r of rankings()"><td>{{r.rank}}</td><td>{{r.studentName}}</td><td>{{r.rollNumber}}</td><td>{{r.averageScore}}</td><td><span class="badge">{{r.grade}}</span></td></tr></table></mat-tab></mat-tab-group></div>` })
export class MarksComponent implements OnInit { private api = inject(MarksService); private fb = inject(FormBuilder); rankings = signal<Ranking[]>([]); form = this.fb.nonNullable.group({ studentId: [1, Validators.required], courseId: [1, Validators.required], score: [80, Validators.required] }); ngOnInit(): void { this.load(); } load(): void { this.api.rankings().subscribe(p => this.rankings.set(p.content)); } save(): void { this.api.record(this.form.getRawValue()).subscribe(() => { this.form.reset({studentId: 1, courseId: 1, score: 80}); this.load(); }); } }
