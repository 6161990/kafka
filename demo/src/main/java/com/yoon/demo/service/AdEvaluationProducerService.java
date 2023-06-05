package com.yoon.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdEvaluationProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void post(String topic, Object message) {
        kafkaTemplate.send(topic, message);
     }
}
