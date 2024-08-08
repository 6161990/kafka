package com.yoon.basically.controller;

import com.yoon.basically.service.KafkaSenderService;
import com.yoon.basically.service.MemberRegisterService;
import com.yoon.basically.vo.MyOutputData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SimpleController {

    private final KafkaSenderService kafkaSenderService;
    private final MemberRegisterService memberRegisterService;

    @PostMapping("/producer/async")
    public void async(@RequestParam String msg){
        kafkaSenderService.sendAsyncWithRetry(new MyOutputData(1, msg), 0);
    }

    @PostMapping("/producer/sync")
    public void sync(@RequestParam String msg){
        kafkaSenderService.sendSync(new MyOutputData(1, msg));
    }

    @PostMapping("/register")
    public void register(@RequestParam String name){
        memberRegisterService.register(name);
    }

    @PostMapping("/register2")
    public void register2(@RequestParam String name){
        memberRegisterService.register2(name);
    }

    @PostMapping("/send_depends_on_input_data")
    public void sendDependsOnInputData(@RequestParam String name){
        memberRegisterService.sendDependsOnInputData(name);
    }
}
