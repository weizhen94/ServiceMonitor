package com.example.service_monitor_backend.service;

import com.example.service_monitor_backend.config.ServiceConfigLoader;
import com.example.service_monitor_backend.model.MonitoredService;
import com.example.service_monitor_backend.model.ServiceHealth;
import com.example.service_monitor_backend.repo.JsonHealthRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Service
public class HealthCheckService {

    private final ServiceConfigLoader configLoader;
    private final JsonHealthRepository healthRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    public HealthCheckService(ServiceConfigLoader configLoader,
                          JsonHealthRepository healthRepository,
                          ObjectMapper objectMapper) {
    this.configLoader = configLoader;
    this.healthRepository = healthRepository;
    this.objectMapper = objectMapper;
    }


    public void checkAll() {
        List<MonitoredService> services = configLoader.getServices();
        for (MonitoredService svc : services) {
            ServiceHealth health = checkOne(svc);
            healthRepository.upsert(health);
        }
    }

    private ServiceHealth checkOne(MonitoredService svc) {
        ServiceHealth health = new ServiceHealth();
        health.setName(svc.getName());
        health.setUrl(svc.getUrl());
        health.setEnvironment(svc.getEnvironment());
        health.setExpectedVersion(svc.getExpectedVersion());
        health.setLastCheckedAt(Instant.now());

        long start = System.currentTimeMillis();
        boolean up = false;
        String actualVersion = null;

        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(svc.getUrl(), String.class);
            long elapsed = System.currentTimeMillis() - start;
            health.setLatencyMs(elapsed);

            if (resp.getStatusCode().is2xxSuccessful()) {
                up = true;
                String body = resp.getBody();
                if (body != null && !body.isEmpty()) {
                    JsonNode root = objectMapper.readTree(body);
                    if (root.has("version")) {
                        actualVersion = root.get("version").asText();
                    } else if (root.has("build") && root.get("build").has("version")) {
                        actualVersion = root.get("build").get("version").asText();
                    }
                }
            } else {
                health.setLatencyMs(System.currentTimeMillis() - start);
            }
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;
            health.setLatencyMs(elapsed);
            up = false;
        }

        health.setUp(up);
        health.setActualVersion(actualVersion);
        health.setVersionDrift(svc.getExpectedVersion() != null
                && actualVersion != null
                && !svc.getExpectedVersion().equals(actualVersion));


        int prev = healthRepository.findAll().stream()
                .filter(h -> h.getName().equals(svc.getName()))
                .map(ServiceHealth::getConsecutiveFailures)
                .findFirst().orElse(0);

        if (!up) {
            health.setConsecutiveFailures(prev + 1);
            if (health.getConsecutiveFailures() >= 3) {
                System.out.println("[ALERT] " + svc.getName() + " DOWN 3 times in a row");
            }
        } else {
            health.setConsecutiveFailures(0);
        }

        return health;
    }

    public List<ServiceHealth> getAllHealth() {
        return healthRepository.findAll();
    }

    public ServiceHealth getHealthByName(String name) {
        return healthRepository.findAll().stream()
                .filter(h -> h.getName().equals(name))
                .findFirst().orElse(null);
    }
}

