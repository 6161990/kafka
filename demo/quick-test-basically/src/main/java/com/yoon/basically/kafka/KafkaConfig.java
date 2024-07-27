package com.yoon.basically.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapConsumerServers;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<Integer, Object> producerFactory(){
        DefaultKafkaProducerFactory<Integer, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
//        producerFactory.setTransactionIdPrefix("tx-");
        return producerFactory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        // FixedBackOff를 사용하여 재시도 간격과 최대 재시도 횟수를 설정합니다.
        // 예: 1000ms 간격으로 최대 3번 재시도
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3);
        return new DefaultErrorHandler(fixedBackOff);
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> prog = new HashMap<>();
        prog.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        prog.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        prog.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return prog;
    }

    @Bean
    public ConsumerFactory<Integer, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapConsumerServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // "com.yoon.basically.kafka" : 역직렬화할 수 있는 패키지
        //configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.yoon.basically.kafka.MemberEvent"); // 기본 역직렬
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "true");
        // Kafka 메시지 헤더에 포함된 타입 정보를 사용할지 여부. 헤더에 타입 정보가 없거나 타입 정보를 사용하고 싶지 않은 경우에는 이 옵션을 비활성화해야함. 설정된 기본 타입으로 역직렬화 시도할 수 있음.

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Integer, Object> kafkaTemplate(KafkaProducerListener kafkaProducerListener) {
        KafkaTemplate<Integer, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        return kafkaTemplate;
    }

    /** kafkaListener 가 concurrently 하게 consumer Factory 정보를 listening */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, Object> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<Integer, Object> cklc = new ConcurrentKafkaListenerContainerFactory<>();
        cklc.setConsumerFactory(consumerFactory());
        cklc.setCommonErrorHandler(errorHandler());
        cklc.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return cklc;
    }
}
