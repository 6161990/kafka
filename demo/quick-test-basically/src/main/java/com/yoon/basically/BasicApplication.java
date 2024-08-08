package com.yoon.basically;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.admin.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BasicApplication {

    @Configuration
    public static class KafkaContainerConfig {

        private KafkaContainer kafkaContainer;
        private AdminClient adminClient;

        @PostConstruct
        public void startKafkaContainer() throws ExecutionException, InterruptedException {
            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
            kafkaContainer.start();
            System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
            System.setProperty("spring.kafka.consumer.bootstrap-servers", kafkaContainer.getBootstrapServers());

//            settingPartition();
        }

        private void settingPartition() throws InterruptedException, ExecutionException {
            Properties config = new Properties();
            config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
            adminClient = AdminClient.create(config);

            // 토픽 생성 및 파티션 설정
            NewTopic newTopic = new NewTopic("topicA", 2, (short) 1); // 파티션 수 2로 설정
            NewTopic newTopic2 = new NewTopic("topicB", 2, (short) 1); // 파티션 수 2로 설정
            adminClient.createTopics(List.of(newTopic, newTopic2));

            // 파티션 갯수 확인
            DescribeTopicsResult result = adminClient.describeTopics(List.of("topicA", "topicB"));
            Map<String, TopicDescription> descriptions = result.all().get();
            int partitionCount = descriptions.get("topicA").partitions().size();
            System.out.println("Partition count for topic 'topicA': " + partitionCount);
            int partitionCount2 = descriptions.get("topicB").partitions().size();
            System.out.println("Partition count for topic 'topicB': " + partitionCount2);
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
