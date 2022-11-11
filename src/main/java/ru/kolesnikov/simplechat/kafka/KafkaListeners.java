package ru.kolesnikov.simplechat.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static ru.kolesnikov.simplechat.kafka.KafkaTopicConfig.KAFKA_TOPIC;

@Component
@Slf4j
public class KafkaListeners {

    @KafkaListener(topics = KAFKA_TOPIC,
            groupId = "myGroupId")
    public boolean listener(boolean data) {
        if(data) {
            log.info("this user was successfully authorized ");
        } else {
            log.info("this user wasn`t authorized ");

        }
        return data;
    }
}
