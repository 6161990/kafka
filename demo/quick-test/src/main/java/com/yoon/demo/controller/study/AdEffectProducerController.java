package com.yoon.demo.controller.study;

import com.yoon.demo.service.AdEvaluationProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdEffectProducerController {

    private final AdEvaluationProducerService adEvaluationProducerService;

    @PostMapping("/message")
    public void PublishMessage(@RequestParam String msg) {
        adEvaluationProducerService.sendJoinedMsg(msg);
    }
}
