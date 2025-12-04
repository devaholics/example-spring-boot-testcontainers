package io.devaholics.example.spring_boot_testcontainers.dev;

import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

@Profile("dev")
@TestConfiguration(proxyBeanMethods = false)
public class DevDatabaseConfiguration {

  //@formatter:off
  /// The definition for a PostgreSQL database started through Testcontainers.
  ///
  ///  Further documentation:
  ///  * [Using Testcontainers at Development Time](https://docs.spring.io/spring-boot/reference/features/dev-services.html#features.dev-services.testcontainers.at-development-time)
  ///
  /// #### A Note About DataSource Initialization
  /// There are three ways to initialize the data source for a database running through Testcontainers:
  ///
  /// ###### 1. Service Connections (preferred)
  /// The easiest way to initialize the data source is to use the @ServiceConnection annotation.
  /// This annotation will automatically configure the data source bean with the connection properties of the container bean.
  /// See the reference documentations about ServiceConnections for more details:
  /// * [Testcontainers - Service Connections](https://docs.spring.io/spring-boot/reference/testing/testcontainers.html#testing.testcontainers.service-connections)
  /// * [Dev Services - Service Connections](https://docs.spring.io/spring-boot/reference/features/dev-services.html#features.dev-services.docker-compose.service-connections)
  ///
  /// ###### 2. DynamicPropertyRegistrar
  /// If you need more control over the data source properties, you can use a DynamicPropertyRegistrar to register them.
  ///
  /// Here is a minimal example that works similar to what the @ServiceConnection annotation does:
  /// ```java
  ///@Bean
  /// DynamicPropertyRegistrar devDatabaseDataSourceProperties(PostgreSQLContainer<?>container){
  ///  return (properties) -> {
  ///    properties.add("spring.datasource.url", container::getJdbcUrl);
  ///    properties.add("spring.datasource.username", container::getUsername);
  ///    properties.add("spring.datasource.password", container::getPassword);
  ///  };
  ///}
  /// ```
  ///
  /// In tests, you might also want to use [dynamic property sources](https://docs.spring.io/spring-boot/reference/testing/testcontainers.html#testing.testcontainers.dynamic-properties).
  ///
  /// ###### 3. Using DataSourceProperties when Creating the Container
  /// Another way is to use an existing DataSourceProperties bean (created automatically by having datasource properties configured) when creating the container.
  ///
  /// To do this, add the DataSourceProperties bean as a dependency when creating the container definition bean:
  /// ```java
  /// @Bean
  /// PostgreSQLContainer<?> devDatabaseContainer(DataSourceProperties dataSourceProperties) {
  ///   return new PostgreSQLContainer<>("postgres:latest")
  ///     .withUsername(dataSourceProperties.getUsername())
  ///     .withPassword(dataSourceProperties.getPassword())
  ///     ...
  /// ```
  ///
  //@formatter:on
  @Bean
  @ServiceConnection
  @SuppressWarnings("resource")
  PostgreSQLContainer<?> devDatabaseContainer() {
    return new PostgreSQLContainer<>("postgres:18")
      .withDatabaseName("some-database")
      .withUsername("sensible-database-username")
      .withPassword("sensible-database-password")
      .withCreateContainerCmdModifier(cmd -> cmd
        .withName("tc-some-fancy-database")
        .withHostConfig(HostConfig.newHostConfig()
          .withAutoRemove(true) //enables faster cleanup of the container
          .withPortBindings(PortBinding.parse("5432:5432"))
          .withBinds(Bind.parse("tc-some-fancy-database-data:/var/lib/postgresql"))
        )
      )
      .waitingFor(postgresDatabaseToBeReady());
  }

  /// A wait strategy that correctly waits for a PostgreSQL database to be ready no matter if the container/database is empty or already contains data.
  ///
  /// References:
  /// * [Main GitHub Issue](https://github.com/testcontainers/testcontainers-java/issues/5359)
  /// * [Latest PostgreSQLContainer implementation](https://tinyurl.com/3pc84fj9)
  private static WaitStrategy postgresDatabaseToBeReady() {
    // The pg_isready utility (https://www.postgresql.org/docs/current/app-pg-isready.html) check works reliably and fast.
    // By sleeping before running pg_isready and using the database name and username as environment variables as arguments, no FATAL appear in the containers' logs.
    // If you don't care about FATAL log messages, you could just use 'pg_isready' without sleeping or additional arguments.
    return Wait.forSuccessfulCommand("sleep 0.5 && pg_isready -d $POSTGRES_DB -U $POSTGRES_USER");
  }

}
