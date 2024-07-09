package com.yoon.demo.service;

import com.yoon.demo.config.KafkaConfigForAdEffectService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdEvaluationProducerService {

    private static final String DEFAULT_TOPIC = "defaultTopic";

    private final KafkaConfigForAdEffectService kafkaConfigForAdEffectService;
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void postForAllPurchaseLog(String topic, Object message) {
        kafkaTemplate = kafkaConfigForAdEffectService.kafkaTemplateForAllPurchaseLog();
        kafkaTemplate.send(topic, message);
     }

    public void postForWatchingAdLog(String topic, Object message) {
        kafkaTemplate = kafkaConfigForAdEffectService.kafkaTemplateForWatchingAdLog();
        kafkaTemplate.send(topic, message);
    }

    public void postForPurchaseOneLog(String topic, Object message) {
        kafkaTemplate = kafkaConfigForAdEffectService.KafkaTemplateForPurchaseOneLog();
        kafkaTemplate.send(topic, message);
    }

    public void sendJoinedMsg(Object message) {
        kafkaTemplate = kafkaConfigForAdEffectService.KafkaTemplateForPurchaseOneLog();
        kafkaTemplate.send(DEFAULT_TOPIC, message);
    }
}
