package io.devaholics.example.spring_boot_testcontainers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SomeFancyServerApplicationTests {

  @Test
  void contextLoads() {
  }

}
