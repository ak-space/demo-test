package com.sgg.demo.event;

import com.sgg.demo.dto.EventRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

@Component
public class CustomEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventPublisher.class);

    @Autowired
    public CustomEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishRecordCreatedEvent(EventRecord eventRecord) {
        Assert.notNull(eventRecord, "EventRecord must not be null");
        LOGGER.trace("Publishing RecordCreatedEvent. ");
        EventRecord eventRecordCopy = eventRecord.toBuilder().build();
        applicationEventPublisher.publishEvent(new RecordCreatedEvent(eventRecordCopy));
    }

    public void publishRecordProcessedEvent(UUID recordId) {
        Assert.notNull(recordId, "RecordId must not be null");
        LOGGER.trace("Publishing RecordProcessedEvent");
        applicationEventPublisher.publishEvent(new RecordProcessedEvent(recordId));
    }

}
