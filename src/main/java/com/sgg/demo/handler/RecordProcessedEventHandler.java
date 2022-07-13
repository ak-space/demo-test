package com.sgg.demo.handler;

import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.event.RecordProcessedEvent;
import com.sgg.demo.exception.EntityNotFoundException;
import com.sgg.demo.service.EventRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RecordProcessedEventHandler {

    private final EventRecordService eventRecordService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordProcessedEventHandler.class);

    @Autowired
    public RecordProcessedEventHandler(EventRecordService eventRecordService) {
        this.eventRecordService = eventRecordService;
    }

    @Async
    @EventListener(classes = RecordProcessedEvent.class)
    public void handle(RecordProcessedEvent event) {

        if (Objects.isNull(event) || Objects.isNull(event.getSource())) {
            LOGGER.warn("Handled RecordProcessedEvent - event or event data not present");
            return;
        }

        try {
            EventRecord eventRecord = eventRecordService.get(event.getSource());
            if (eventRecord != null) {
                eventRecord.setProcessed(true);
                eventRecordService.update(eventRecord);
            }
        } catch (EntityNotFoundException e) {
            LOGGER.error("Unable to handle RecordProcessedEvent for record id:{}", event.getSource());
        }

    }
}