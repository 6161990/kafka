package com.yoon.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoon.demo.vo.PurchaseLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class PurchaseOneLogSerializer implements Serializer<PurchaseLog> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, PurchaseLog data) {
        try {
            if(data==null){
                log.warn("data is null");
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        }catch (Exception e){
            throw new SecurityException("Error when serializing.");
        }
    }
}
