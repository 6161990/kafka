package com.yoon.basically.controller;

import com.yoon.basically.service.KafkaSenderService;
import com.yoon.basically.vo.MyOutputData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SimpleProducerController {

    private final KafkaSenderService kafkaSenderService;

    @PostMapping("/producer/async")
    public void async(@RequestParam String msg){
        kafkaSenderService.sendAsyncWithRetry(new MyOutputData(1, msg), 0);
    }

    @PostMapping("/producer/sync")
    public void sync(@RequestParam String msg){
        kafkaSenderService.sendSync(new MyOutputData(1, msg));
    }
}
