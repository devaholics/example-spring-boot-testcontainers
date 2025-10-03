package io.devaholics.example.spring_boot_testcontainers;

import org.springframework.boot.SpringApplication;

public class TestSomeFancyServerApplication {

  static void main(String[] args) {
    SpringApplication.from(SomeFancyServerApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
