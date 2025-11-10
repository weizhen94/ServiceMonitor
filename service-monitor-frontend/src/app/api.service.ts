import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ServiceHealth {
  name: string;
  url: string;
  environment: string;
  up: boolean;
  latencyMs: number;
  expectedVersion: string;
  actualVersion: string;
  versionDrift: boolean;
  consecutiveFailures: number;
  lastCheckedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient) {}

  getHealth(): Observable<ServiceHealth[]> {
    return this.http.get<ServiceHealth[]>(`${this.baseUrl}/health`);
  }

  getSummary(): Observable<string> {
    return this.http.get(`${this.baseUrl}/summary`, { responseType: 'text' });
  }
}
