package com.yoon.basically.service;

import com.yoon.basically.domain.Member;
import com.yoon.basically.repository.MemberRepository;
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

        kafkaSenderService.send(member);
    }
}
