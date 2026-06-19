import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../../../core/services/auth.service';
import { Role } from '../../../core/models/api.models';

interface NavItem { label: string; icon: string; route: string; roles?: Role[]; }

@Component({
  selector: 'sp-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, MatButtonModule, MatIconModule, MatListModule, MatSidenavModule, MatToolbarModule],
  template: `
  <mat-sidenav-container class="shell">
    <mat-sidenav mode="side" opened class="sidebar">
      <div class="brand"><mat-icon>school</mat-icon><span>Student Portal</span></div>
      <mat-nav-list>
        <a mat-list-item *ngFor="let item of visibleNav()" [routerLink]="item.route" routerLinkActive="active-link">
          <mat-icon matListItemIcon>{{ item.icon }}</mat-icon>
          <span matListItemTitle>{{ item.label }}</span>
        </a>
      </mat-nav-list>
      <div class="user-card">
        <strong>{{ auth.user()?.username }}</strong>
        <span>{{ auth.user()?.role }}</span>
      </div>
    </mat-sidenav>
    <mat-sidenav-content>
      <mat-toolbar color="primary">
        <span class="toolbar-title">Academic Operations Dashboard</span>
        <span class="spacer"></span>
        <button mat-button routerLink="/profile"><mat-icon>account_circle</mat-icon> Profile</button>
        <button mat-button (click)="auth.logout()"><mat-icon>logout</mat-icon> Logout</button>
      </mat-toolbar>
      <router-outlet />
    </mat-sidenav-content>
  </mat-sidenav-container>
  `,
  styles: [`
    .shell { min-height: 100vh; }
    .sidebar { width: 260px; background: #1a237e; color: #fff; border-radius: 0; }
    .brand { height: 64px; display: flex; align-items: center; gap: 12px; padding: 0 20px; font-size: 20px; font-weight: 700; }
    .sidebar a { color: #fff; }
    .active-link { background: rgba(255,255,255,.16); }
    .user-card { position: absolute; bottom: 0; left: 0; right: 0; padding: 16px 20px; background: rgba(0,0,0,.18); display: grid; }
    .toolbar-title { font-weight: 600; }
    .spacer { flex: 1; }
  `]
})
export class LayoutComponent {
  readonly auth = inject(AuthService);
  readonly nav: NavItem[] = [
    { label: 'Dashboard', icon: 'dashboard', route: '/dashboard', roles: ['ADMIN', 'FACULTY'] },
    { label: 'Students', icon: 'groups', route: '/students', roles: ['ADMIN', 'FACULTY'] },
    { label: 'Courses', icon: 'menu_book', route: '/courses' },
    { label: 'Attendance', icon: 'fact_check', route: '/attendance', roles: ['ADMIN', 'FACULTY'] },
    { label: 'Marks', icon: 'grade', route: '/marks', roles: ['ADMIN', 'FACULTY'] },
    { label: 'Announcements', icon: 'campaign', route: '/announcements' },
    { label: 'Study Materials', icon: 'folder', route: '/study-materials' },
    { label: 'Reports', icon: 'bar_chart', route: '/reports', roles: ['ADMIN', 'FACULTY'] },
    { label: 'Profile', icon: 'person', route: '/profile' }
  ];

  visibleNav(): NavItem[] {
    return this.nav.filter(item => !item.roles || this.auth.hasAnyRole(item.roles));
  }
}
