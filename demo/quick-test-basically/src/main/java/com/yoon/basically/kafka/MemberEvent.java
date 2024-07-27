package com.yoon.basically.kafka;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberEvent {
    private EventType eventType;

    private Long id;


}
