package com.yoon.demo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 어떤 유저가 어떤 상품을 얼마의 가격으로 언제 구매했는
 * */
@Data
public class PurchaseLog {
    String orderId; // oId-0001
    String userId; // uId-0001
    List<Map<String,String>> productInfos; // [{"productId":"pg-0001","price":"24000"}]
    String purchasedDt; // 20230201070000
}
