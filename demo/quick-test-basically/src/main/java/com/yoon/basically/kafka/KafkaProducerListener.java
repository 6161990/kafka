package com.yoon.basically.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducerListener implements ProducerListener {
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("==== onSuccess ====");
        log.info("record body : {}", producerRecord.value());
        log.info("record header : {}", producerRecord.headers());
        log.info("record topic : {}", recordMetadata.topic());
        log.info("record offset : {}", recordMetadata.offset());
    }

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.info("==== onError ====");
        log.info("record body : {}", producerRecord.value());
        log.info("record header : {}", producerRecord.headers());
        log.info("record topic : {}", recordMetadata.topic());
        log.info("record offset : {}", recordMetadata.offset());
        log.error("record exception : {}", exception.getMessage());
    }
}
