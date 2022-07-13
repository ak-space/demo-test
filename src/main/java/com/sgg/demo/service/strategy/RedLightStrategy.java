package com.sgg.demo.service.strategy;

import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.service.ViolationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedLightStrategy implements Strategy{
    private static final String STRATEGY_NAME_TRAFFIC_RED_LIGHT= "REDLIGHT";
    private static final Double DEFAULT_TRAFFIC_RED_LIGHT_FINE = 100d;
    private static final Logger LOGGER = LoggerFactory.getLogger(RedLightStrategy.class);
    private final ViolationService violationService;

    @Autowired
    public RedLightStrategy(ViolationService violationService) {
        this.violationService = violationService;
    }

    @Override
    public void check(EventRecord eventRecord) {
        LOGGER.info("Strategy invoke type:{}", STRATEGY_NAME_TRAFFIC_RED_LIGHT);
        Optional.ofNullable(eventRecord)
                .ifPresentOrElse(e ->violationService.create(e.getId(), DEFAULT_TRAFFIC_RED_LIGHT_FINE),
                        ()-> LOGGER.error("RedLightStrategy invoke - record is null"));
    }

    @Override
    public String getStrategyName() {
        return STRATEGY_NAME_TRAFFIC_RED_LIGHT;
    }
}
