package io.devaholics.example.spring_boot_testcontainers;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SomeFancyJdbcRepository extends CrudRepository<SomeFancyEntity, UUID> {
}
