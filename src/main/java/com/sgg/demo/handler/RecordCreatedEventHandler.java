package com.sgg.demo.handler;

import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.event.CustomEventPublisher;
import com.sgg.demo.event.RecordCreatedEvent;
import com.sgg.demo.service.strategy.ViolationStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RecordCreatedEventHandler {

    private final ViolationStrategyFactory violationStrategyFactory;
    private final CustomEventPublisher eventPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordCreatedEventHandler.class);

    @Autowired
    public RecordCreatedEventHandler(ViolationStrategyFactory violationStrategyFactory, CustomEventPublisher eventPublisher) {
        this.violationStrategyFactory = violationStrategyFactory;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @EventListener(classes = RecordCreatedEvent.class)
    public void handle(RecordCreatedEvent event) {

        if (Objects.isNull(event) || Objects.isNull(event.getSource())) {
            LOGGER.warn("Handled RecordCreatedEvent - event or event data not present");
            return;
        }

        EventRecord eventRecord = event.getSource();
        LOGGER.trace("Handled RecordCreatedEvent - record id:{}", eventRecord.getId());

        violationStrategyFactory.getStrategyFor(eventRecord.getEventType())
                .ifPresentOrElse(
                        s -> s.check(eventRecord),
                        () -> LOGGER.trace("No strategy for handling - type:{} record id:{}", eventRecord.getEventType(), eventRecord.getId())
                );

        eventPublisher.publishRecordProcessedEvent(eventRecord.getId());
    }
}