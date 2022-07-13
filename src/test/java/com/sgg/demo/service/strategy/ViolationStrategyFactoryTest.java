package com.sgg.demo.service.strategy;

import com.sgg.demo.dto.EventRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
public class ViolationStrategyFactoryTest {

    @InjectMocks
    private ViolationStrategyFactory violationStrategyFactory;
    private final static String TEST_STRATEGY_NAME = "TEST";

    @Before
    public void before() {
        Strategy strategy = new Strategy() {
            public void check(EventRecord eventRecord) {
            }
            public String getStrategyName() {
                return TEST_STRATEGY_NAME;
            }
        };
        violationStrategyFactory = new ViolationStrategyFactory(List.of(strategy));
    }

    @Test
    public void getStrategyFor_getStrategy_strategyPresent() {

        Optional<Strategy> test = violationStrategyFactory.getStrategyFor(TEST_STRATEGY_NAME);
        assertTrue(test.isPresent());
        assertNotNull(test.get());
        assertEquals(TEST_STRATEGY_NAME, test.get().getStrategyName());

    }

    @Test
    public void getStrategyFor_getStrategy_strategyNotPresent() {

        Optional<Strategy> test = violationStrategyFactory.getStrategyFor("TEST2");
        assertFalse(test.isPresent());

    }

    @Test
    public void getStrategyFor_getStrategyByLowerCase_strategyPresent() {

        Optional<Strategy> test = violationStrategyFactory.getStrategyFor("test");
        assertTrue(test.isPresent());
        assertNotNull(test.get());
        assertEquals(TEST_STRATEGY_NAME, test.get().getStrategyName());

    }


}
