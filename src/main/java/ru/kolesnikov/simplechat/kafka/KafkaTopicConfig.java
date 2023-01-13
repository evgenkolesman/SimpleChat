package ru.kolesnikov.simplechat.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String KAFKA_TOPIC = "EvgenKafkaCode";

    @Bean
    public NewTopic kafkaTopic() {
        return TopicBuilder.name(KAFKA_TOPIC)
                .replicas(1)
                .partitions(1)
                .build();
    }
}
