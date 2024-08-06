package com.yoon.basically.kafka;

import com.yoon.basically.vo.MyOutputData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Collections;

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
//             acknowledgment.acknowledge();
        }
    }

    @KafkaListener(groupId = "test-group", topics = "register2")
    public void listen(@Payload MemberEvent memberEvent, ConsumerRecord<?, ?> consumerRecord, Consumer<?, ?> consumer) {
        try {
            log.info("===== MyKafkaEventListener: [register2] Received a message =====");
            consumer.commitSync(Collections.singletonMap(
                    new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
                    new OffsetAndMetadata(consumerRecord.offset() + 1)
            ));
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the message: {}", memberEvent, e);
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
            // acknowledgment.acknowledge();
        }
    }
}
