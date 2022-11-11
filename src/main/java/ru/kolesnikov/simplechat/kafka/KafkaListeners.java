package ru.kolesnikov.simplechat.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static ru.kolesnikov.simplechat.kafka.KafkaTopicConfig.KAFKA_TOPIC;

@Component
@Slf4j
public class KafkaListeners {

    @KafkaListener(topics = KAFKA_TOPIC,
            groupId = "myGroupId")
    void listener(@Payload String data) {
        log.info(data);
    }
}
