# Example: Spring Boot + Testcontainers for Development

This repository demonstrates how to use Testcontainers together with Spring Boot for a smooth development experience.

It spins up a Postgres database and a Mailpit SMTP server automatically when you run the app through `bootTestRun`.

Key ideas showcased:

- Run infrastructure services on-demand via Testcontainers during development.
- Use Spring Boot's Service Connection (@ServiceConnection) to autoconfigure a data source.
- Register dynamic properties for non-standard services (Mailpit) at runtime.
- Keep your local machine clean — only a named volume for database-data is left when the app is stopped.

## Talk
- This repository was showcased during the "Reducing Friction with Testcontainers" talk by [Philipp Sommersguter](https://www.linkedin.com/in/philipp-sommersguter/),
presented on behalf of [Devaholics](https://devaholics.io/) at the [Java User Group Graz](https://www.meetup.com/java-user-group-graz/) Revival Meetup.
- **Slides** can be found [here](./slides). While the code in this repository is constantly evolving, the slides represent the state of this repository as of 03.12.2025.

## Prerequisites

- Docker (or a compatible container runtime) must be installed before running the project.
- While running, the project must be allowed to interact with the container runtime. See:
  - [Manage Docker as a non-root user](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user)
  - [Testcontainers with Podman](https://podman-desktop.io/tutorial/testcontainers-with-podman)

## Quickstart

Start the project through Gradle by running:

  ```shell
  ./gradlew bootTestRun
  ```

After that, you will have a working database connection and mail service, both powered by Testcontainers.

You can manually test this by calling the endpoints in [exposed through the RestController.](./src/main/java/io/devaholics/example/spring_boot_testcontainers/SomeFancyRestController.java)

## Notable Implementation Details

- This project was
  created [by using this Spring Initializr configuration](https://start.spring.io/#!type=gradle-project-kotlin&language=java&platformVersion=3.5.6&packaging=jar&jvmVersion=25&groupId=com.demo&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=com.demo.demo&dependencies=web,data-jpa,postgresql,testcontainers,mail)
  and adapted with some personal preferences like the "dev" profile and the style of configuration classes.
- This project uses Postgres
  in [version 18, which changed the layout of the data directory](https://hub.docker.com/_/postgres#pgdata). Beware that
  adaptions are necessary when using older versions
- A custom wait strategy is used to wait for Postgres to be ready. This is required because the default wait strategy
  that checks on logs of the container cannot correctly handle prefilled databases. Though this is Postgres specific
  behavior and might not be necessary when using other database vendors.
- The database container binds to the fixed host port `5432` to easily allow database explorers to interact with the
  database when it is running.
- The database container also uses a named volume for persistence between runs. The volume needs to be deleted manually
  when a clean state is desired.
- The Mailpit container exposes its SMTP and Web UI on dynamic ports on the host. The corresponding Spring Mail
  properties are correctly configured at runtime.
- Please note that it is necessary to set the `spring.mail.host` in
  the [application.properties](./src/main/resources/application.properties)
  to trigger the MailSenderAutoConfiguration.
  See [Spring Boot issue #35127](https://github.com/spring-projects/spring-boot/issues/35127#issuecomment-3372670541)
  for more details.

## References

- Testcontainers:
  - https://testcontainers.com/
  - https://java.testcontainers.org/
- Spring Boot Testcontainers & Dev Services:
  - https://docs.spring.io/spring-boot/reference/testing/testcontainers.html
  - https://docs.spring.io/spring-boot/reference/features/dev-services.html
- Mailpit: https://mailpit.axllent.org/

## License

The slides are licensed under a [CC-BY license](LICENSE).

All other code in this repository is licensed under the [MIT license](LICENSE-CODE).
