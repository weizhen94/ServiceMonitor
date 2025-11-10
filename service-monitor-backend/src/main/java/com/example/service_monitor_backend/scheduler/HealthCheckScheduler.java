package com.example.service_monitor_backend.scheduler;

import com.example.service_monitor_backend.service.HealthCheckService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckScheduler {

    private final HealthCheckService healthCheckService;

    public HealthCheckScheduler(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @Scheduled(fixedRateString = "30000")
    public void runChecks() {
        healthCheckService.checkAll();
    }
}

