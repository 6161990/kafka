package com.yoon.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleConsumerService {

    @KafkaListener(topics = "yoon", groupId = "com")
    public void consuming(String message){
        System.out.printf("Consuming : %S%n", message);
    }
}
