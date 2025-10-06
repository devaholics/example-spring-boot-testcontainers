package io.devaholics.example.spring_boot_testcontainers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/fancy")
public class SomeFancyRestController {

  private final SomeFancyService service;

  public SomeFancyRestController(SomeFancyService service) {
    this.service = service;
  }

  @GetMapping("/database-content")
  public Stream<String> getDatabaseTable() {
    return service.streamAllAsStringLines();
  }

  @GetMapping("/mailpit-open")
  public ResponseEntity<String> openMailpit() {
    return ResponseEntity.status(302)
      .location(service.buildMailpitWebUiUri())
      .build();
  }

  @GetMapping("/testmail-send")
  public String sendTestMail() {
    service.sendTestMail();
    return "Test mail sent successfully";
  }

}
