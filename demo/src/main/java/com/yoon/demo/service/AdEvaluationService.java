package com.yoon.demo.service;

import lombok.Data;

/**
 * 광고 데이터 중복 Join 될 필요 없다.
 * 광고 이력이 먼저 들어온다.
 * 구매 이력은 상품별로 들어오지 않는다. (복수 개의 상품 존재) --> join
 * 광고에 머무른 시간이 10초 이상이어야한다.
 * 특정 가격 이상의 상품은 Join 될 필요 없다.
 * 광고 이력 KTable (AdLog)
 * 구매 이력 KStream (OrderLog)
 * filtering, 형변환
 * EffectOrNot json 형태로 --> Topic : AdEvaluationComplete
 */
@Data
public class AdEvaluationService {
}
