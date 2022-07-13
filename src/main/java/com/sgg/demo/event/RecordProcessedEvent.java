package com.sgg.demo.event;

import java.util.UUID;

public class RecordProcessedEvent extends AbstractEvent<UUID> {
    public RecordProcessedEvent(UUID recordId) {
        super(recordId);
    }

}
