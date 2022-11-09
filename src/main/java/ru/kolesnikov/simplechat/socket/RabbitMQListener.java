package ru.kolesnikov.simplechat.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
//@Component
//@Slf4j
//public class RabbitMQListener {
//
//    @RabbitListener(queues = "queue1")
//    public void processQueue1(String message) {
//        log.info("Received from queue 1: " + message);
//    }
//}
