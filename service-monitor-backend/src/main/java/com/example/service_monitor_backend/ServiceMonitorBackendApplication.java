package com.example.service_monitor_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceMonitorBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceMonitorBackendApplication.class, args);
	}

}
