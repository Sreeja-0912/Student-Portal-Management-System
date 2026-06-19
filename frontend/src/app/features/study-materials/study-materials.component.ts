import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { StudyMaterial } from '../../core/models/api.models';
import { AuthService } from '../../core/services/auth.service';
import { StudyMaterialService } from '../../core/services/study-material.service';

@Component({ selector: 'sp-study-materials', standalone: true, imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule], template: `
<div class="page"><h1>Study Materials</h1><mat-card *ngIf="auth.hasAnyRole(['ADMIN'])"><mat-card-content><form [formGroup]="form" (ngSubmit)="save()" class="form-grid"><mat-form-field appearance="outline"><mat-label>Course ID</mat-label><input matInput type="number" formControlName="courseId"></mat-form-field><mat-form-field appearance="outline"><mat-label>Title</mat-label><input matInput formControlName="title"></mat-form-field><mat-form-field appearance="outline"><mat-label>File URL</mat-label><input matInput formControlName="fileUrl"></mat-form-field><button mat-raised-button color="primary" [disabled]="form.invalid">Add</button></form></mat-card-content></mat-card><table><tr><th>Course</th><th>Title</th><th>Link</th></tr><tr *ngFor="let m of materials()"><td>{{m.courseCode}} - {{m.courseName}}</td><td>{{m.title}}</td><td><a mat-button [href]="m.fileUrl" target="_blank">Open</a></td></tr></table></div>` })
export class StudyMaterialsComponent implements OnInit { private api = inject(StudyMaterialService); private fb = inject(FormBuilder); readonly auth = inject(AuthService); materials = signal<StudyMaterial[]>([]); form = this.fb.nonNullable.group({ courseId: [1, Validators.required], title: ['', Validators.required], fileUrl: ['', Validators.required] }); ngOnInit(): void { this.load(); } load(): void { this.api.list().subscribe(p => this.materials.set(p.content)); } save(): void { this.api.create(this.form.getRawValue()).subscribe(() => { this.form.reset({courseId: 1}); this.load(); }); } }
