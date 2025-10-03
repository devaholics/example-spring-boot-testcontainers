package io.devaholics.example.spring_boot_testcontainers;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Table("fancy")
public record SomeFancyEntity(@Id @Nullable UUID id, String columnA, String columnB) {
}
