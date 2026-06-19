import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AuditLog, Page } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class AuditService {
  private http = inject(HttpClient);
  list() { return this.http.get<Page<AuditLog>>(`${environment.apiUrl}/audit-logs?size=50`); }
}
