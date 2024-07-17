package com.yoon.basically.service;

import com.yoon.basically.AbstractTestcontainersTest;
import com.yoon.basically.config.KafkaConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import java.util.concurrent.atomic.AtomicReference;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(KafkaConfig.class)
public class KafkaConsumerServiceTest extends AbstractTestcontainersTest {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    private static final AtomicReference<String> receivedMessage = new AtomicReference<>();
    private static final String TOPIC = "hello-world";

    @Autowired
    public KafkaConsumerServiceTest(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(groupId = "test-group", topics = TOPIC)
    public void listen(String message) {
        receivedMessage.set(message);
    }

    @Test
    void sendAndReceive() {
        ProducerRecord<Integer, String> record = new ProducerRecord<>(TOPIC, 1, "쀼");
        kafkaTemplate.send(record);

        Awaitility.await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(receivedMessage.get()).isEqualTo("쀼")
                );
    }

}
