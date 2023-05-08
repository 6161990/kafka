package com.yoon.demo.controller;

import com.yoon.demo.service.SimpleProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SimpleProducerController {

    private final SimpleProducerService simpleProducerService;

    @PostMapping("/producer")
    public void post(@RequestParam String msg){
        simpleProducerService.post(msg);
    }
}
