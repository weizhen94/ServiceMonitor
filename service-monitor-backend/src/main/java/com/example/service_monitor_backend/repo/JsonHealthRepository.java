package com.example.service_monitor_backend.repo;

import com.example.service_monitor_backend.model.ServiceHealth;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JsonHealthRepository {

    private final ObjectMapper objectMapper;
    private final File file = new File("health.json");

    public JsonHealthRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public synchronized List<ServiceHealth> findAll() {
        try {
            if (!file.exists()) return new ArrayList<>();
            byte[] jsonData = Files.readAllBytes(file.toPath());
            return objectMapper.readValue(jsonData, new TypeReference<List<ServiceHealth>>() {});
        } catch (Exception e) {
            e.printStackTrace();

            try {
                Path p = file.toPath();
                Files.move(p, p.resolveSibling("health.json.corrupt-" + System.currentTimeMillis()),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignore) {}
            return new ArrayList<>();
        }
    }

    public synchronized void saveAll(List<ServiceHealth> list) {
        try {
            Path target = file.toPath();
            Path tmp = target.resolveSibling(file.getName() + ".tmp");

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), list);

            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void upsert(ServiceHealth health) {
        List<ServiceHealth> all = findAll();
        all.removeIf(h -> h.getName().equals(health.getName()));
        all.add(health);
        saveAll(all);
    }
}


