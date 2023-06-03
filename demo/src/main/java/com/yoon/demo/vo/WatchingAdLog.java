package com.yoon.demo.vo;

import lombok.Data;

/**
 * 어떤 유저가 어떤 상품의 어떤 광고를 어떻게 언제 보았는지
 * {
 *   "userId": "uId-0001",
 *   "productId": "pg-0001",
 *   "adId": "ad-101",
 *   "adType": "banner",
 *   "watchingDt": "20230201070000"
 * }
 * */
@Data
public class WatchingAdLog {
    String userId; // uId-0001
    String productId; // pg-0001
    String adId; // ad-101
    String adType; // banner, clip,
    String watchingTime; // 11412s
    String watchingDt; // 20230201070000
}
