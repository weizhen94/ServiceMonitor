import { Component, OnInit } from '@angular/core';
import { ApiService, ServiceHealth } from '../api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  health: ServiceHealth[] = [];
  summary = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.load();

    setInterval(() => this.load(), 30000);
  }

  load() {
    this.api.getHealth().subscribe(data => {
      this.health = (data || []).sort((a, b) => (a.environment || '').localeCompare(b.environment || ''));
    });
    this.api.getSummary().subscribe(txt => this.summary = txt);
  }

  statusClass(h: ServiceHealth) {
    return h.up ? 'status up' : 'status down';
  }

  driftLabel(h: ServiceHealth) {
    return h.versionDrift ? 'DRIFT' : 'OK';
  }
}
