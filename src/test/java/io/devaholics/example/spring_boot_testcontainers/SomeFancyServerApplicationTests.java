package io.devaholics.example.spring_boot_testcontainers;

import io.devaholics.example.spring_boot_testcontainers.dev.DevDatabaseConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(DevDatabaseConfiguration.class)
@SpringBootTest
@ActiveProfiles("dev")
class SomeFancyServerApplicationTests {

  @Test
  void contextLoads() {
  }

}
