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

import java.util.*;

/**
 * -- 광고 데이터 중복 Join 될 필요 없다.
 * 광고 이력이 먼저 들어온다.
 * 구매 이력은 상품별로 들어오지 않는다. (복수 개의 상품 존재) --> join
 * 광고에 머무른 시간이 10초 이상이어야한다.
 * 특정 가격 이상의 상품은 Join 될 필요 없다.
 * 광고 이력 KTable (AdLog)
 * 구매 이력 KTable (AllPurchaseLog)
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
                .stream("AllPurchaseLog", Consumed.with(Serdes.String(), purchaseLogSerde));

        purchaseLogKStream.foreach((key, value) -> {
            for (Map<String, String> prodInfo: value.getProductInfos()) {
                if (Integer.parseInt(prodInfo.get("price")) < 1000000){
                    PurchaseOneLog purchaseOneLog = PurchaseOneLog.builder()
                            .userId(value.getUserId())
                            .orderId(value.getOrderId())
                            .price(Integer.parseInt(prodInfo.get("price")))
                            .productId(prodInfo.get("productId"))
                            .purchasedDt(value.getPurchasedDt())
                            .build();
                    adEvaluationProducerService.postForPurchaseOneLog("PurchaseOneLog", purchaseOneLog);
                }
            }
        });

        KTable<String, PurchaseOneLog> purchaseOneLogKTable = builder
                .stream("PurchaseOneLog", Consumed.with(Serdes.String(), purchaseOneLogSerde))
                .selectKey((k,v)-> v.getUserId() + "_" + v.getProductId())
                .toTable(Materialized.<String, PurchaseOneLog, KeyValueStore<Bytes, byte[]>>as("PurchaseOneStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(purchaseOneLogSerde));

        ValueJoiner<WatchingAdLog, PurchaseOneLog, EffectedLog> valueJoiner
                = (left, right) -> {

            Map<String, String> productInfoMap = new HashMap<>();
            productInfoMap.put("productId",right.getProductId());
            productInfoMap.put("price", String.valueOf(right.getPrice()));

            return EffectedLog.builder()
                    .adId(left.getAdId())
                    .userId(right.getUserId())
                    .orderId(right.getOrderId())
                    .productInfo(productInfoMap)
                    .build();
        };


        watchingAdLogKTable.join(purchaseOneLogKTable, valueJoiner)
                .toStream()
                .to("AdEvaluationComplete", Produced.with(Serdes.String(), effectOrNotSerde));
    }

    public void sendNewMsg() {
        PurchaseLog tempPurchaseLog  = new PurchaseLog();
        WatchingAdLog tempWatchingAdLog = new WatchingAdLog();

        //랜덤한 ID를 생성하기 위해 아래의 함수를 사용합니다.
        // random Numbers for concatenation with attrs
        Random rd = new Random();
        int rdUidNumber = rd.nextInt(9999);
        int rdOrderNumber = rd.nextInt(9999);
        int rdProdIdNumber = rd.nextInt(9999);
        int rdPriceIdNumber = rd.nextInt(90000)+10000;
        int prodCnt = rd.nextInt(9)+1;
        int watchingTime = rd.nextInt(55)+5;

        // bind value for purchaseLog
        tempPurchaseLog.setUserId("uid-" + String.format("%05d", rdUidNumber));
        tempPurchaseLog.setPurchasedDt("20230101070000");
        tempPurchaseLog.setOrderId("od-" + String.format("%05d", rdOrderNumber));
        List<Map<String, String>> tempProdInfo = new ArrayList<>();
        Map<String, String> tempProd = new HashMap<>();
        for (int i=0; i<prodCnt; i++ ){
            tempProd.put("productId", "pg-" + String.format("%05d", rdProdIdNumber));
            tempProd.put("price", String.format("%05d", rdPriceIdNumber));
            tempProdInfo.add(tempProd);
        }
        tempPurchaseLog.setProductInfos(tempProdInfo);

        // bind value for watchingAdLog
        tempWatchingAdLog.setUserId("uid-" + String.format("%05d", rdUidNumber));
        tempWatchingAdLog.setProductId("pg-" + String.format("%05d", rdProdIdNumber));
        tempWatchingAdLog.setAdId("ad-" + String.format("%05d",rdUidNumber) );
        tempWatchingAdLog.setAdType("banner");
        tempWatchingAdLog.setWatchingTime(String.valueOf(watchingTime));
        tempWatchingAdLog.setWatchingDt("20230201070000");

        // produce msg
        adEvaluationProducerService.postForAllPurchaseLog("AllPurchaseLog", tempPurchaseLog);
        adEvaluationProducerService.postForWatchingAdLog("AdLog", tempWatchingAdLog);
    }

}
