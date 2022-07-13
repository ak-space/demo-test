package com.sgg.demo.service.strategy;
import com.sgg.demo.dto.EventRecord;
public interface Strategy {
    void check(EventRecord eventRecord);
    String getStrategyName();
}
