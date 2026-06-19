import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, Role } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  readonly user = signal<AuthResponse | null>(this.readUser());
  readonly token = signal<string | null>(localStorage.getItem('sp_token'));
  readonly isLoggedIn = computed(() => !!this.token());

  login(request: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request).pipe(tap(res => this.persist(res)));
  }

  register(request: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, request).pipe(tap(res => this.persist(res)));
  }

  logout(): void {
    localStorage.removeItem('sp_token');
    localStorage.removeItem('sp_user');
    this.user.set(null);
    this.token.set(null);
    this.router.navigate(['/login']);
  }

  hasAnyRole(roles: readonly string[]): boolean {
    const role = this.user()?.role;
    return !!role && roles.includes(role);
  }

  private persist(res: AuthResponse): void {
    localStorage.setItem('sp_token', res.token);
    localStorage.setItem('sp_user', JSON.stringify(res));
    this.user.set(res);
    this.token.set(res.token);
  }

  private readUser(): AuthResponse | null {
    const raw = localStorage.getItem('sp_user');
    return raw ? JSON.parse(raw) as AuthResponse : null;
  }
}
