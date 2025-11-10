package com.example.service_monitor_backend.config;

import com.example.service_monitor_backend.model.MonitoredService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
public class ServiceConfigLoader {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<MonitoredService> services = Collections.emptyList();

    public ServiceConfigLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        load();
    }

    public final void load() {
        try (InputStream is = new ClassPathResource("services.json").getInputStream()) {
            this.services = objectMapper.readValue(is, new TypeReference<List<MonitoredService>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            this.services = Collections.emptyList();
        }
    }

    public List<MonitoredService> getServices() {
        return services;
    }
}

