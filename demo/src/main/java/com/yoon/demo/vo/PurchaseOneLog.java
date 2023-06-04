package com.yoon.demo.vo;

import lombok.Builder;

@Builder
public class PurchaseOneLog {
    String orderId; // oId-0001
    String userId; // uId-0001
    String productId; // pg-0001
    String purchasedDt; // 20230201070000
    Long price;
}
