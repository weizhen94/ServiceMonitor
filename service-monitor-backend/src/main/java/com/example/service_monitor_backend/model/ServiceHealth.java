package com.example.service_monitor_backend.model;

import java.time.Instant;

public class ServiceHealth {
    private String name;
    private String url;
    private String environment;
    private boolean up;
    private long latencyMs;
    private String expectedVersion;
    private String actualVersion;
    private boolean versionDrift;
    private int consecutiveFailures;
    private Instant lastCheckedAt;

    public ServiceHealth() {}

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getEnvironment() { return environment; }
    public boolean isUp() { return up; }
    public long getLatencyMs() { return latencyMs; }
    public String getExpectedVersion() { return expectedVersion; }
    public String getActualVersion() { return actualVersion; }
    public boolean isVersionDrift() { return versionDrift; }
    public int getConsecutiveFailures() { return consecutiveFailures; }
    public Instant getLastCheckedAt() { return lastCheckedAt; }

    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public void setUp(boolean up) { this.up = up; }
    public void setLatencyMs(long latencyMs) { this.latencyMs = latencyMs; }
    public void setExpectedVersion(String expectedVersion) { this.expectedVersion = expectedVersion; }
    public void setActualVersion(String actualVersion) { this.actualVersion = actualVersion; }
    public void setVersionDrift(boolean versionDrift) { this.versionDrift = versionDrift; }
    public void setConsecutiveFailures(int consecutiveFailures) { this.consecutiveFailures = consecutiveFailures; }
    public void setLastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }
}

