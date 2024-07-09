package com.yoon.demo.config;

import com.yoon.demo.utils.AllPurchaseLogSerializer;
import com.yoon.demo.utils.PurchaseOneLogSerializer;
import com.yoon.demo.utils.WatchingAdLogSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfigForAdEffectService {

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplateForAllPurchaseLog(){
        return new KafkaTemplate<>(producerFactoryForAllPurchaseLog());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactoryForAllPurchaseLog(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "3.42.324.235");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AllPurchaseLogSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplateForWatchingAdLog(){
        return new KafkaTemplate<>(producerFactoryForWatchingAdLog());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactoryForWatchingAdLog(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "3.42.324.235");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, WatchingAdLogSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> KafkaTemplateForPurchaseOneLog() {
        return new KafkaTemplate<String, Object>(producerFactoryForPurchaseOneLog());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactoryForPurchaseOneLog() {
        Map<String, Object> myConfig = new HashMap<>();

        myConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "13.125.205.11:9092, 3.36.63.75:9092, 54.180.1.108:9092");
        myConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        myConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PurchaseOneLogSerializer.class);

        return new DefaultKafkaProducerFactory<>(myConfig);
    }
}
