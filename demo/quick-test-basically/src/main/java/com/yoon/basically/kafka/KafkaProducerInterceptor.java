package com.yoon.basically.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaProducerInterceptor implements ProducerInterceptor<Integer, Object> {

    @Override
    public ProducerRecord<Integer, Object> onSend(ProducerRecord<Integer, Object> record) {
        log.info("==== onSend ====");
        log.info("record body : {}", record.value());
        log.info("record header : {}", record.headers());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        log.info("==== onAcknowledgement ====");
        log.info("topic : {}", metadata.topic());
        log.info("partition : {}", metadata.partition());
        if (exception != null) {
            log.error("error message: {}", exception.getMessage());
        }
    }

    @Override
    public void close() {
        log.info("==== close ====");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.info("==== configure ====");
        log.info("configure :{}", configs);
    }
}
