package io.devaholics.example.spring_boot_testcontainers.dev;

import io.devaholics.example.spring_boot_testcontainers.SomeFancyServerApplication;
import org.springframework.boot.SpringApplication;

public class DevSomeFancyServerApplication {

  @SuppressWarnings("UnnecessaryModifier") // Necessary until Spring Boot 3.5.7 is released on the 23.10.2025 which supports package-private main methods
  public static void main(String[] args) {
    SpringApplication.from(SomeFancyServerApplication::main)
      .with(DevDatabaseConfiguration.class, DevMailpitConfiguration.class)
      .run(args);
  }

}
