package io.devaholics.example.spring_boot_testcontainers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
        randomAlphabeticWord(5) + "-" + i,
        randomAlphabeticWord(ThreadLocalRandom.current().nextInt(10, 20))
      ));
    }
    log.info("Database filled successfully");

  }

  private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  private static String randomAlphabeticWord(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("size must be > 0");
    }

    var stringBuilder = new StringBuilder(size);
    var random = ThreadLocalRandom.current();
    for (int i = 0; i < size; i++) {
      stringBuilder.append(ALPHABET[random.nextInt(ALPHABET.length)]);
    }
    return stringBuilder.toString();
  }

}
