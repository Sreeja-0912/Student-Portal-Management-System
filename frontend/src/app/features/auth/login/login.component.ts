import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'sp-login',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatIconModule, MatInputModule],
  template: `
  <div class="login-wrap">
    <mat-card class="login-card">
      <mat-card-header>
        <mat-card-title>Student Portal Management System</mat-card-title>
        <mat-card-subtitle>Default: admin / Admin&#64;123</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="form" (ngSubmit)="submit()" class="login-form">
          <mat-form-field appearance="outline"><mat-label>Username</mat-label><input matInput formControlName="username"></mat-form-field>
          <mat-form-field appearance="outline"><mat-label>Password</mat-label><input matInput [type]="hide ? 'password' : 'text'" formControlName="password"><button mat-icon-button matSuffix type="button" (click)="hide=!hide"><mat-icon>{{ hide ? 'visibility' : 'visibility_off' }}</mat-icon></button></mat-form-field>
          <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Login</button>
        </form>
      </mat-card-content>
    </mat-card>
  </div>
  `,
  styles: [`
    .login-wrap { min-height: 100vh; display: grid; place-items: center; background: linear-gradient(135deg, #1a237e, #3949ab); }
    .login-card { width: min(420px, 92vw); }
    .login-form { margin-top: 20px; display: grid; gap: 12px; }
  `]
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  hide = true;
  form = this.fb.nonNullable.group({ username: ['admin', Validators.required], password: ['Admin@123', Validators.required] });

  submit(): void {
    if (this.form.invalid) return;
    this.auth.login(this.form.getRawValue()).subscribe(() => this.router.navigate(['/dashboard']));
  }
}
