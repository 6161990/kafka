package com.yoon.demo.service.study;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleProducerService {

    private static final String topic = "yoon";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void post(String message) {
        kafkaTemplate.send(topic, message);
     }
}
