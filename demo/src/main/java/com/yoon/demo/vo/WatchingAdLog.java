package com.yoon.demo.vo;

import lombok.Data;

/**
 * 어떤 유저가 어떤 상품의 어떤 광고를 어떻게 언제 보았는지
 * */
@Data
public class WatchingAdLog {
    String userId; // uId-0001
    String productId; // pg-0001
    String adId; // ad-101
    String adType; // banner, clip,
    String watchingDt; // 20230201070000
}
