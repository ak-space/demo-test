package com.sgg.demo.handler;

import com.sgg.demo.BaseTestHelper;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.event.CustomEventPublisher;
import com.sgg.demo.event.RecordCreatedEvent;
import com.sgg.demo.service.strategy.Strategy;
import com.sgg.demo.service.strategy.ViolationStrategyFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecordCreatedEventHandlerTest extends BaseTestHelper {

    @Mock
    private CustomEventPublisher eventPublisher;
    @Mock
    private ViolationStrategyFactory violationStrategyFactory;
    @Mock
    private Strategy strategy;
    @InjectMocks
    private RecordCreatedEventHandler recordCreatedEventHandler;
    private static final String TEST_STRATEGY_NAME = "TEST";

    @Test
    public void handle_checkEventRecord_strategyAndPublisherInvoked() {

        EventRecord eventRecord = EventRecord.builder()
                .id(ID)
                .eventType("TEST")
                .licensePlate("TestLicensePlate")
                .limit(50)
                .speed(100)
                .unity("km/h")
                .processed(false)
                .eventDate(CONST_DATE)
                .build();

        when(violationStrategyFactory.getStrategyFor(TEST_STRATEGY_NAME)).thenReturn(Optional.of(strategy));
        recordCreatedEventHandler.handle(new RecordCreatedEvent(eventRecord));
        Mockito.verify(violationStrategyFactory,only()).getStrategyFor(eventRecord.getEventType());
        Mockito.verify(strategy,only()).check(eventRecord);
        Mockito.verify(eventPublisher,only()).publishRecordProcessedEvent(ID);

    }

    @Test
    public void handle_checkEventRecord_strategyAndNotInvoked() {

        EventRecord eventRecord = EventRecord.builder()
                .id(ID)
                .eventType("TEST")
                .licensePlate("TestLicensePlate")
                .limit(50)
                .speed(100)
                .unity("km/h")
                .processed(false)
                .eventDate(CONST_DATE)
                .build();

        when(violationStrategyFactory.getStrategyFor(TEST_STRATEGY_NAME)).thenReturn(Optional.empty());
        recordCreatedEventHandler.handle(new RecordCreatedEvent(eventRecord));
        Mockito.verify(violationStrategyFactory,only()).getStrategyFor(TEST_STRATEGY_NAME);
        Mockito.verify(strategy,never()).check(any());
        Mockito.verify(eventPublisher,only()).publishRecordProcessedEvent(ID);
    }
}
