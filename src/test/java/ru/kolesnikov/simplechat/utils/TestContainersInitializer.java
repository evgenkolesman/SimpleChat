package ru.kolesnikov.simplechat.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class TestContainersInitializer {

    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:6.0.3"));
    public static final PostgreSQLContainer<?> postgresDBContainer =
            new PostgreSQLContainer<>("postgres:14.3-alpine");
    public static final KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.0"));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresDBContainer.getUsername(),
                    "spring.datasource.password=" + postgresDBContainer.getPassword(),
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),
                    "spring.data.mongodb.host=" + mongoDBContainer.getHost(),
                    "spring.data.mongodb.port=" + mongoDBContainer.getFirstMappedPort(),
                    "spring.data.mongodb.url=" + mongoDBContainer.getReplicaSetUrl()
            ).applyTo(applicationContext);
        }
    }
}
