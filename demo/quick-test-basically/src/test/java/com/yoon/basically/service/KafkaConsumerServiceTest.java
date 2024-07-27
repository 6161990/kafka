package com.yoon.basically.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoon.basically.AbstractTestcontainersTest;
import com.yoon.basically.kafka.KafkaConfig;
import com.yoon.basically.vo.MyOutputData;
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

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    private static final AtomicReference<Object> receivedMessage = new AtomicReference<>();
    private static final String TOPIC = "hello-world";

    @Autowired
    public KafkaConsumerServiceTest(KafkaTemplate<Integer, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(groupId = "test-group", topics = TOPIC)
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MyOutputData myOutputData = objectMapper.readValue(message, MyOutputData.class);
        receivedMessage.set(myOutputData);
    }

    @Test
    void sendAndReceive() {
        MyOutputData myOutputData = new MyOutputData(1, "쀼");
        ProducerRecord<Integer, Object> record = new ProducerRecord<>(TOPIC, myOutputData.key(), myOutputData);
        kafkaTemplate.send(record);

        Awaitility.await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(receivedMessage.get()).isEqualTo(myOutputData)
                );
    }

}
