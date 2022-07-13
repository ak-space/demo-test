package com.sgg.demo.service;

import com.sgg.demo.exception.EntityNotFoundException;
import com.sgg.demo.dto.EventRecord;

import java.util.Collection;
import java.util.UUID;

public interface EventRecordService {
    EventRecord get(UUID id);
    EventRecord save(EventRecord eventRecord);
    Collection<EventRecord> findAll();
    void update(EventRecord eventRecord) throws EntityNotFoundException;
    void removeAll();
}
