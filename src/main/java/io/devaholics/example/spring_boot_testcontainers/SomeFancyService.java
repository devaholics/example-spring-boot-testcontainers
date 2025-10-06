package io.devaholics.example.spring_boot_testcontainers;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SomeFancyService {

  private final SomeFancyJdbcRepository repository;
  private final MailSender mailSender;
  private final MailProperties mailProperties;

  public SomeFancyService(SomeFancyJdbcRepository repository,
                          MailProperties mailProperties,
                          MailSender mailSender) {
    this.repository = repository;
    this.mailProperties = mailProperties;
    this.mailSender = mailSender;
  }

  public Stream<String> streamAllAsStringLines() {
    var allEntities = repository.findAll();
    return StreamSupport.stream(allEntities.spliterator(), true).map(SomeFancyEntity::toString);
  }

  public URI buildMailpitWebUiUri() {
    var mailpitWebPort = mailProperties.getProperties().get("mailpit.web.port");
    if (mailpitWebPort == null) {
      throw new IllegalStateException("mailpit.web.port is not set");
    }
    return URI.create("http://%s:%s/".formatted(mailProperties.getHost(), mailpitWebPort));
  }

  public void sendTestMail() {
    var now = ZonedDateTime.now();

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("noreply@fancy-server.com");
    mailMessage.setTo("WhomItMayConcern@whatever.com");
    mailMessage.setSubject("Testmail (%s)".formatted(now));
    mailMessage.setText("This is a testmail coming from a Spring Boot application sent at %s".formatted(now));
    mailSender.send(mailMessage);
  }
}
