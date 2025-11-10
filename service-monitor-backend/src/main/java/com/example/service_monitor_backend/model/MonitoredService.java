package com.example.service_monitor_backend.model;

public class MonitoredService {
    private String name;
    private String url;
    private String expectedVersion;
    private String environment;

    public MonitoredService() {}

    public MonitoredService(String name, String url, String expectedVersion, String environment) {
        this.name = name;
        this.url = url;
        this.expectedVersion = expectedVersion;
        this.environment = environment;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getExpectedVersion() { return expectedVersion; }
    public String getEnvironment() { return environment; }

    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }
    public void setExpectedVersion(String expectedVersion) { this.expectedVersion = expectedVersion; }
    public void setEnvironment(String environment) { this.environment = environment; }
}
