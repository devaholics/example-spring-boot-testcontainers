package io.devaholics.example.spring_boot_testcontainers.dev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;

@Profile("dev")
@TestConfiguration(proxyBeanMethods = false)
public class DevMailpitConfiguration {

  private static final Logger log = LogManager.getLogger();

  /// A container running the [Mailpit](https://mailpit.axllent.org/) service.
  @Bean
  @SuppressWarnings("resource")
  GenericContainer<?> mailpitContainer() {
    return new GenericContainer<>("axllent/mailpit:v1.27")
      .withCreateContainerCmdModifier(cmd -> cmd
        .withName("tc-some-fancy-mailpit")
      )
      .withExposedPorts(1025, 8025);
  }

  @Bean
  DynamicPropertyRegistrar mailpitPropertiesRegistrar(GenericContainer<?> mailpitContainer) {
    log.info("Mailpit UI is accessible through http://{}:{}", mailpitContainer.getHost(), mailpitContainer.getMappedPort(8025));

    return (properties) -> {
      properties.add("spring.mail.host", mailpitContainer::getHost);
      properties.add("spring.mail.port", () -> mailpitContainer.getMappedPort(1025));
      properties.add("spring.mail.properties.mailpit.web.port", () -> mailpitContainer.getMappedPort(8025));
    };
  }

}
