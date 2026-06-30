import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout.component';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent) },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', canActivate: [roleGuard], data: { roles: ['ADMIN', 'FACULTY'] }, loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'students', canActivate: [roleGuard], data: { roles: ['ADMIN', 'FACULTY'] }, loadComponent: () => import('./features/students/students.component').then(m => m.StudentsComponent) },
      { path: 'courses', loadComponent: () => import('./features/courses/courses.component').then(m => m.CoursesComponent) },
      { path: 'attendance', canActivate: [roleGuard], data: { roles: ['ADMIN', 'FACULTY','STUDENT'] }, loadComponent: () => import('./features/attendance/attendance.component').then(m => m.AttendanceComponent) },
      { path: 'marks', canActivate: [roleGuard], data: { roles: ['ADMIN', 'FACULTY','STUDENT'] }, loadComponent: () => import('./features/marks/marks.component').then(m => m.MarksComponent) },
      { path: 'announcements', loadComponent: () => import('./features/announcements/announcements.component').then(m => m.AnnouncementsComponent) },
      { path: 'study-materials', loadComponent: () => import('./features/study-materials/study-materials.component').then(m => m.StudyMaterialsComponent) },
      { path: 'reports', canActivate: [roleGuard], data: { roles: ['ADMIN', 'FACULTY'] }, loadComponent: () => import('./features/reports/reports.component').then(m => m.ReportsComponent) },
      { path: 'profile', loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent) }
    ]
  },
  { path: '**', redirectTo: '' }
];
