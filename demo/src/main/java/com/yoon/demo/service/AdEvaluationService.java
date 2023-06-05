package com.yoon.demo.service;

import com.yoon.demo.vo.EffectedLog;
import com.yoon.demo.vo.PurchaseLog;
import com.yoon.demo.vo.PurchaseOneLog;
import com.yoon.demo.vo.WatchingAdLog;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

/**
 * -- 광고 데이터 중복 Join 될 필요 없다.
 * 광고 이력이 먼저 들어온다.
 * 구매 이력은 상품별로 들어오지 않는다. (복수 개의 상품 존재) --> join
 * 광고에 머무른 시간이 10초 이상이어야한다.
 * 특정 가격 이상의 상품은 Join 될 필요 없다.
 * 광고 이력 KTable (AdLog)
 * 구매 이력 KStream (OrderLog)
 * filtering, 형변환
 * EffectOrNot json 형태로 --> Topic : AdEvaluationComplete
 */
@Service
@RequiredArgsConstructor
public class AdEvaluationService {

    private final AdEvaluationProducerService adEvaluationProducerService;

    @Autowired
    public void buildPipeline(StreamsBuilder builder){
        // producing 하거나 consume 할 때 필요한 serializer creating
        JsonSerializer<EffectedLog> effectOrNotJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<EffectedLog> effectOrNotJsonDeserializer = new JsonDeserializer<>();

        JsonSerializer<PurchaseLog> purchaseLogJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<PurchaseLog> purchaseLogJsonDeserializer = new JsonDeserializer<>();

        JsonSerializer<PurchaseOneLog> purchaseOneLogJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<PurchaseOneLog> purchaseOneLogJsonDeserializer = new JsonDeserializer<>();

        JsonSerializer<WatchingAdLog> watchingAdLogJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<WatchingAdLog> watchingAdLogJsonDeserializer = new JsonDeserializer<>();


        Serde<EffectedLog> effectOrNotSerde = Serdes.serdeFrom(effectOrNotJsonSerializer, effectOrNotJsonDeserializer);
        Serde<PurchaseLog> purchaseLogSerde = Serdes.serdeFrom(purchaseLogJsonSerializer, purchaseLogJsonDeserializer);
        Serde<WatchingAdLog> watchingAdLogSerde = Serdes.serdeFrom(watchingAdLogJsonSerializer, watchingAdLogJsonDeserializer);
        Serde<PurchaseOneLog> purchaseOneLogSerde = Serdes.serdeFrom(purchaseOneLogJsonSerializer, purchaseOneLogJsonDeserializer);

        // AdLog Stream --> KTable
        KTable<String, WatchingAdLog> watchingAdLogKTable = builder
                .stream("AdLog", Consumed.with(Serdes.String(), watchingAdLogSerde)) // KEY : OBJECT
                .selectKey((k,v)-> v.getUserId() + "_" + v.getProductId())
                .toTable(Materialized.<String, WatchingAdLog, KeyValueStore<Bytes, byte[]>>as("adStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(watchingAdLogSerde));
        // table 속성 : key value 스토어로 데이터를 넣었다 뺐다 할 수 있고 캐싱할 수도 있음.

        KStream<String, PurchaseLog> purchaseLogKStream = builder
                .stream("OrderLog", Consumed.with(Serdes.String(), purchaseLogSerde));

        purchaseLogKStream.foreach((key, value) -> {
            for (String prodId: value.getProductIds()) {
                if (value.getPrice() < 1000000){
                    PurchaseOneLog purchaseOneLog = PurchaseOneLog.builder()
                            .userId(value.getUserId())
                            .orderId(value.getOrderId())
                            .price(value.getPrice())
                            .productId(prodId)
                            .purchasedDt(value.getPurchasedDt())
                            .build();
                    adEvaluationProducerService.post("oneProduct", purchaseOneLog);
                }
            }
        });

        KStream<String, PurchaseOneLog> purchaseOneLogKStream = builder
                .stream("oneProduct", Consumed.with(Serdes.String(), purchaseOneLogSerde))
                .selectKey((k,v)-> v.getUserId() + "_" + v.getProductId());

        ValueJoiner<WatchingAdLog, PurchaseLog, EffectedLog> valueJoiner
                = (left, right) ->
                EffectedLog.builder()
                            .adId(left.getAdId())
                            .userId(right.getUserId())
                            .build();

    }
}
