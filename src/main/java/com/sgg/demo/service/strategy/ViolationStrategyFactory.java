package com.sgg.demo.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ViolationStrategyFactory {
    private final Map<String,Strategy> strategyMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Logger LOGGER = LoggerFactory.getLogger(ViolationStrategyFactory.class);

    @Autowired
    public ViolationStrategyFactory(List<Strategy> strategies) {
        applyStrategies(strategies);
    }
    public Optional<Strategy> getStrategyFor(String name){
        return Optional.ofNullable(strategyMap.get(name));

    }
    private void applyStrategies(Collection<Strategy> strategies) {
        if(strategies == null || strategies.isEmpty()){
            LOGGER.warn("Violation strategies is empty");
            return;
        }
        LOGGER.trace("Applying set of violation strategies size:{}",strategies.size());
        strategies.forEach(s->strategyMap.put(s.getStrategyName(),s));

    }
}
