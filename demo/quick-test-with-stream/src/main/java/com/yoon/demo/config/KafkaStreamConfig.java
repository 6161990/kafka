package com.yoon.demo.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME) /** 스마트 라이프를 상속받아서 스트림 빌더를 넣어주는 역할 */
    public KafkaStreamsConfiguration kafkaStreamConfig(){
        Map<String, Object> map = new HashMap<>();
        map.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-test"); /** 별칭 */
        map.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "3.42.324.235"); /** public Ipv4 주소*/
        map.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        map.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        map.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3); /** 한번에 몇 개의 스레드까지 열 것 인가 */
        map.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");/** 프로듀서가 사용하는 acks 옵션은 간단하게 말해 프로듀서가 메시지를 보내고 그 메시지를 카프카가 잘 받았는지 확인을 할 것인지 또는 확인을 하지 않을 것인지를 결정하는 옵션*/
        map.put(StreamsConfig.topicPrefix(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG), 2);/** 적어도 2개의 리플리카 싱크가 설정되었을 때 수행하겠다.*/
        map.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);
        return new KafkaStreamsConfiguration(map);
    }
}
