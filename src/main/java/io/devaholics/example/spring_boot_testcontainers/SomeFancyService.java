package io.devaholics.example.spring_boot_testcontainers;

import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SomeFancyService {

  private final SomeFancyJdbcRepository repository;

  public SomeFancyService(SomeFancyJdbcRepository repository) {
    this.repository = repository;
  }

  public Stream<String> streamAllAsStringLines() {
    var allEntities = repository.findAll();
    return StreamSupport.stream(allEntities.spliterator(), true).map(SomeFancyEntity::toString);
  }

}
