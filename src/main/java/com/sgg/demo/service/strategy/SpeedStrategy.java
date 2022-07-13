package com.sgg.demo.service.strategy;

import com.sgg.demo.service.ViolationService;
import com.sgg.demo.dto.EventRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpeedStrategy implements Strategy{

    private static final String STRATEGY_NAME_SPEED = "SPEED";
    private static final Double DEFAULT_SPEED_FINE = 50d;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedStrategy.class);
    private final ViolationService violationService;

    @Autowired
    public SpeedStrategy(ViolationService violationService) {
        this.violationService = violationService;
    }

    @Override
    public void check(EventRecord eventRecord) {
        Optional.ofNullable(eventRecord)
                .ifPresentOrElse(e -> {
                    LOGGER.trace("Strategy invoke name:{} record id{}", STRATEGY_NAME_SPEED, e.getId());
                    if(e.getSpeed()>e.getLimit()) {
                        violationService.create(e.getId(), DEFAULT_SPEED_FINE);
                    }},
                    ()-> LOGGER.error("Strategy invoke name:{} record is null", STRATEGY_NAME_SPEED));
    }

    @Override
    public String getStrategyName() {
        return STRATEGY_NAME_SPEED;
    }
}