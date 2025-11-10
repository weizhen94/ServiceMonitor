package com.example.service_monitor_backend.controller;

import com.example.service_monitor_backend.config.ServiceConfigLoader;
import com.example.service_monitor_backend.model.MonitoredService;
import com.example.service_monitor_backend.model.ServiceHealth;
import com.example.service_monitor_backend.service.HealthCheckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ServiceController {

    private final ServiceConfigLoader configLoader;
    private final HealthCheckService healthCheckService;

    public ServiceController(ServiceConfigLoader configLoader,
                             HealthCheckService healthCheckService) {
        this.configLoader = configLoader;
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/services")
    public List<MonitoredService> getServices() {
        return configLoader.getServices();
    }

    @GetMapping("/health")
    public List<ServiceHealth> getHealth() {
        return healthCheckService.getAllHealth();
    }

    @GetMapping("/health/{name}")
    public ServiceHealth getHealthByName(@PathVariable String name) {
        return healthCheckService.getHealthByName(name);
    }

    @PostMapping("/services/reload")
    public void reload() {
        configLoader.load();
        healthCheckService.checkAll();
    }
}

