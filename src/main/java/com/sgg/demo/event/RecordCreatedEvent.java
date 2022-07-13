package com.sgg.demo.event;

import com.sgg.demo.dto.EventRecord;

public class RecordCreatedEvent extends AbstractEvent<EventRecord> {
    public RecordCreatedEvent(EventRecord source) {
        super(source);
    }

}
