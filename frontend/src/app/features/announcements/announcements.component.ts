import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Announcement } from '../../core/models/api.models';
import { AnnouncementService } from '../../core/services/announcement.service';
import { AuthService } from '../../core/services/auth.service';

@Component({ selector: 'sp-announcements', standalone: true, imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule], template: `
<div class="page"><h1>Announcements</h1><mat-card *ngIf="auth.hasAnyRole(['ADMIN','FACULTY'])"><mat-card-content><form [formGroup]="form" (ngSubmit)="save()" class="form-grid"><mat-form-field appearance="outline"><mat-label>Title</mat-label><input matInput formControlName="title"></mat-form-field><mat-form-field appearance="outline"><mat-label>Description</mat-label><input matInput formControlName="description"></mat-form-field><button mat-raised-button color="primary" [disabled]="form.invalid">Create</button></form></mat-card-content></mat-card><div class="grid"><mat-card *ngFor="let a of announcements()" style="border-left: 5px solid #1a237e"><mat-card-title>{{a.title}}</mat-card-title><mat-card-subtitle>{{a.createdByName}} · {{a.createdDate | date}}</mat-card-subtitle><p>{{a.description}}</p></mat-card></div></div>` })
export class AnnouncementsComponent implements OnInit { private api = inject(AnnouncementService); private fb = inject(FormBuilder); readonly auth = inject(AuthService); announcements = signal<Announcement[]>([]); form = this.fb.nonNullable.group({ title: ['', Validators.required], description: ['', Validators.required] }); ngOnInit(): void { this.load(); } load(): void { this.api.list().subscribe(p => this.announcements.set(p.content)); } save(): void { this.api.create(this.form.getRawValue()).subscribe(() => { this.form.reset(); this.load(); }); } }
