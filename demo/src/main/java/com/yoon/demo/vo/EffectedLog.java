package com.yoon.demo.vo;

import lombok.Builder;

import java.util.Map;

/**
 * 어떤 광고가 어느 유저에게 영향을 주었는지
 * */
@Builder
public class EffectedLog {
    String adId; // ad-101
    String userId; // uId-0001
    String orderId;
    Map<String, String> productInfo;
}
