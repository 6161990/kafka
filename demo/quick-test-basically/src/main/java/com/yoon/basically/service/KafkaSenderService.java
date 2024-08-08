package com.yoon.basically.service;

import com.yoon.basically.domain.Member;
import com.yoon.basically.kafka.EventType;
import com.yoon.basically.kafka.MemberEvent;
import com.yoon.basically.vo.MyOutputData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSenderService {

    private static final int MAX_RETRY_ATTEMPTS = 3; // 최대 재시도 횟수
    private static final long RETRY_DELAY_MS = 1000; // 재시도 대기 시간 (밀리초)

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    // 비동기 전송 방식
    public void sendAsyncWithRetry(final MyOutputData data, int attempt){
        final ProducerRecord<Integer, Object> record = createRecord(data);

        CompletableFuture<SendResult<Integer, Object>> future = kafkaTemplate.send(record);
        future.whenComplete(((IntegerSendResult, throwable) -> {
            if(throwable == null){
                handleSuccess(data);
            }else {
                if(attempt < MAX_RETRY_ATTEMPTS) {
                    log.warn("Retrying to send message : {}. Attempt: {}", data, attempt + 1);
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        log.error("Retry sleep interrupted", e);
                    }
                    sendAsyncWithRetry(data, attempt + 1);
                }else {
                    handleFailure(data, record, throwable);
                }
            }
        }));
    }

    // 동기 전송 방식
    public void sendSync(final MyOutputData data){
        final ProducerRecord<Integer, Object> record = createRecord(data);

        try {
            kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);
            handleSuccess(data);
        } catch (ExecutionException e) {
            handleFailure(data, record, e.getCause());
        } catch (TimeoutException // 지정된 시간 내에 send 메서드가 완료되지 않았을 때
                 | InterruptedException e) { // 현재 스레드가 실행 중에 인터럽트(중단)되었을 때 발생
            handleFailure(data, record, e);
        }
    }

    private void handleFailure(MyOutputData data, ProducerRecord<Integer, Object> record, Throwable ex) {
        log.error("Failed to send message: {}. Record: {}. Error: {}", data, record, ex.getMessage());
    }

    private void handleSuccess(MyOutputData data) {
        log.info("Message sent successfully: {}", data);
        // 추가 성공 로직
    }

    ProducerRecord<Integer, Object> createRecord(MyOutputData data) {
        Integer key = data.key();

        String topic = "hello-world";
        return new ProducerRecord<>(topic, key, data);
    }


    public void send(MemberEvent member) {
        String topic = "register";
        kafkaTemplate.send(topic, member);
    }

    public void send2(MemberEvent member) {
        String topic = "register2";
        kafkaTemplate.send(topic, member);
    }

    public void sendDependsOnInputData(MyOutputData myOutputData) {
        kafkaTemplate.send(myOutputData.value(), myOutputData);
    }

}
