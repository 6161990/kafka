package com.yoon.basically.kafka;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MemberEvent {
    private EventType eventType;

    private Long id;


}
