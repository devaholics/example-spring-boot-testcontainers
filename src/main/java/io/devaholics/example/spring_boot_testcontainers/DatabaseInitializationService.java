package io.devaholics.example.spring_boot_testcontainers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class DatabaseInitializationService {

  private static final Logger log = LogManager.getLogger();

  private final SomeFancyJdbcRepository fancyJdbcRepository;
  private final JdbcTemplate jdbcTemplate;

  public DatabaseInitializationService(SomeFancyJdbcRepository fancyJdbcRepository, JdbcTemplate jdbcTemplate) {
    this.fancyJdbcRepository = fancyJdbcRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady(ApplicationReadyEvent event) {
    // Initialize database schema according to SomeFancyEntity
    jdbcTemplate.execute("""
      CREATE TABLE IF NOT EXISTS fancy (
        id UUID PRIMARY KEY DEFAULT uuidv4(),
        column_a TEXT NOT NULL,
        column_b TEXT NOT NULL
      )
      """);

    // Fill the database with test data
    if (fancyJdbcRepository.count() > 0) {
      log.info("Database already filled, nothing to do");
      return;
    }

    var desiredEntriesCount = 10;
    log.info("Database is empty. Filling with {} entries...", desiredEntriesCount);
    for (int i = 0; i < desiredEntriesCount; i++) {
      fancyJdbcRepository.save(new SomeFancyEntity(
        null,
        RandomStringUtils.randomAlphabetic(5) + "-" + i,
        RandomStringUtils.randomAlphabetic(ThreadLocalRandom.current().nextInt(10, 20))
      ));
    }
    log.info("Database filled successfully");

  }

}
