package com.yoon.demo.vo;

import lombok.Data;

/**
 * 어떤 유저가 어떤 상품을 얼마의 가격으로 언제 구매했는
 * */
@Data
public class PurchaseLog {
    String userId; // uId-0001
    String productId; // pg-0001
    String purchasedDt; // 20230201070000
    Long price;
}
