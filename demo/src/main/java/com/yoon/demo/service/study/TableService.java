package com.yoon.demo.service.study;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TableService {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    public void buildPipeline(StreamsBuilder builder){
        KTable<String, String> leftTable = builder.stream("leftTopic", Consumed.with(STRING_SERDE, STRING_SERDE)).toTable();
        KTable<String, String> rightTable = builder.stream("rightTopic", Consumed.with(STRING_SERDE, STRING_SERDE)).toTable();

        ValueJoiner<String, String, String> stringJoiner
                = (value1, value2) -> "[StringJoiner]" + value1 + "-" + value2;

        KTable<String, String> joined = leftTable.join(rightTable, stringJoiner);
        joined.toStream().to("joinedMsg");
    }

}
