package com.yoon.demo.config;

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
        return new KafkaStreamsConfiguration(map);
    }
}
