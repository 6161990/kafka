package com.yoon.basically;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BasicApplication {

    @Configuration
    public static class KafkaContainerConfig {

        private KafkaContainer kafkaContainer;

        @PostConstruct
        public void startKafkaContainer() {
            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
            kafkaContainer.start();
            System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
            System.setProperty("spring.kafka.consumer.bootstrap-servers", kafkaContainer.getBootstrapServers());
        }

        @PreDestroy
        public void stopKafkaContainer() {
            if (kafkaContainer != null) {
                kafkaContainer.stop();
            }
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

}
