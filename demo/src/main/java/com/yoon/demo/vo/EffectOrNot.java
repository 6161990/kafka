package com.yoon.demo.vo;

import lombok.Data;

/**
 * 어떤 광고가 어느 유저에게 영향을 주었는지
 * */
@Data
public class EffectOrNot {
    String adId; // ad-101
    String effectiveness; // y,n / 0,1
    String userId; // uId-0001
}
