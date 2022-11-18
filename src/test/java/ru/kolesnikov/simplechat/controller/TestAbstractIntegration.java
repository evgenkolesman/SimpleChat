package ru.kolesnikov.simplechat.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.kolesnikov.simplechat.utils.TestContainersInitializer;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {
        TestContainersInitializer.Initializer.class
})
public abstract class TestAbstractIntegration {

    @BeforeAll
    static void init() {
        TestContainersInitializer.postgresDBContainer.start();
        TestContainersInitializer.kafka.start();
        TestContainersInitializer.mongoDBContainer.start();
    }

}
