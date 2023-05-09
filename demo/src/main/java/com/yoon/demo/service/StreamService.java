package com.yoon.demo.service;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamService {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    public void buildPipeline(StreamsBuilder builder){
        /** key , value : key 는 어떤 파티션에 들어갈지, 한 파티션을 두 개의 파티션으로 나눌지 등의 역할을 할 수 있다.
         * 컨슘한 다음, 변형작업 (필터링 등)을 한 다음에 다른 파티션에 넣어 주는 작업.
         * yoon 에서 iam2 값을 가진 애들을 필터링에서 yoon2 토픽으로 흘려보낸다.
         * */

        KStream<String, String> map = builder.stream("yoon", Consumed.with(STRING_SERDE, STRING_SERDE));
        map.print(Printed.toSysOut());
        map.filter((key, value) -> value.contains("iam2")).to("yoon2");

    }
}
