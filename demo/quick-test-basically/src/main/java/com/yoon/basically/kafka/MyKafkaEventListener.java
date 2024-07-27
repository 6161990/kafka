package com.yoon.basically.kafka;

import com.yoon.basically.vo.MyOutputData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyKafkaEventListener {

    @KafkaListener(groupId = "test-group", topics = "register")
    public void listen(@Payload MemberEvent memberEvent,
                       @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment) {
        log.info("===== MyKafkaEventListener: Received a message =====");

        try {
            log.info("MyKafkaEventListener :: data={}", memberEvent);
            acknowledgment.acknowledge();
            log.info("Message acknowledged successfully.");
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the message: {}", memberEvent, e);
            // You can choose to either acknowledge or not acknowledge the message based on your use case
            // acknowledgment.acknowledge();
        }
    }

    @KafkaListener(groupId = "test-group", topics = "output")
    public void listen(@Payload MyOutputData myOutputData,
                       @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment) {
        log.info("===== MyKafkaEventListener: Received a message =====");

        try {
            log.info("MyKafkaEventListener :: data={}", myOutputData);
            acknowledgment.acknowledge();
            log.info("Message acknowledged successfully.");
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the message: {}", myOutputData, e);
            // You can choose to either acknowledge or not acknowledge the message based on your use case
            // acknowledgment.acknowledge();
        }
    }
}
