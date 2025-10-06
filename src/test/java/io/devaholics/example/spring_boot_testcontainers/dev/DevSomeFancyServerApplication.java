package io.devaholics.example.spring_boot_testcontainers.dev;

import io.devaholics.example.spring_boot_testcontainers.SomeFancyServerApplication;
import org.springframework.boot.SpringApplication;

public class DevSomeFancyServerApplication {

  //TODO file bug for initialization failure on missing "public" access modifier!
  public static void main(String[] args) {
    SpringApplication.from(SomeFancyServerApplication::main).with(DevDatabaseConfiguration.class).run(args);
  }

}
