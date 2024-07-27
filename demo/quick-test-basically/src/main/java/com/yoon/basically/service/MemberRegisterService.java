package com.yoon.basically.service;

import com.yoon.basically.domain.Member;
import com.yoon.basically.kafka.EventType;
import com.yoon.basically.kafka.MemberEvent;
import com.yoon.basically.repository.MemberRepository;
import com.yoon.basically.vo.MyOutputData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberRegisterService {

    private final MemberRepository memberRepository;
    private final KafkaSenderService kafkaSenderService;

    @Transactional
    public void register(String name) {
        Member member = new Member(1L, name);
        memberRepository.save(member);

        kafkaSenderService.send(new MemberEvent(EventType.CREATED, member.getId()));
    }

    @Transactional
    public void register2(String name) {
        Member member = new Member(10L, name);
        memberRepository.save(member);

        kafkaSenderService.send(new MyOutputData(1, name));
    }
}
