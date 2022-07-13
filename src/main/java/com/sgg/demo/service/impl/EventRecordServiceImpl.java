package com.sgg.demo.service.impl;

import com.sgg.demo.event.CustomEventPublisher;
import com.sgg.demo.exception.EntityNotFoundException;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.service.EventRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
public class EventRecordServiceImpl implements EventRecordService {

    private final CustomEventPublisher publisher;
    private final ConcurrentMap<UUID, EventRecord> records;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventRecordServiceImpl.class);

    @Autowired
    public EventRecordServiceImpl(CustomEventPublisher publisher) {
        this(publisher, new ConcurrentHashMap<>());
    }

    public EventRecordServiceImpl(CustomEventPublisher publisher, ConcurrentMap<UUID, EventRecord> records) {
        this.publisher = publisher;
        this.records = records;
    }

    @Override
    public EventRecord get(UUID id) {
        LOGGER.trace("Get record by id:{}", id);
        return records.get(id);
    }

    public EventRecord save(EventRecord eventRecord) {

        eventRecord.setId(UUID.randomUUID());
        eventRecord.setEventDate(LocalDateTime.now());

        records.putIfAbsent(eventRecord.getId(), eventRecord);
        publisher.publishRecordCreatedEvent(eventRecord);
        LOGGER.trace("Record saved id:{}", eventRecord.getId());
        return eventRecord;
    }

    public void update(EventRecord eventRecord) throws EntityNotFoundException {

        if (!records.containsKey(eventRecord.getId())) {
            throw new EntityNotFoundException("Record Entity not found id:" + eventRecord.getId());
        }
        records.computeIfPresent(eventRecord.getId(), (k, v) -> eventRecord);
        LOGGER.trace("Record updated id:{}", eventRecord.getId());
    }

    @Override
    public Collection<EventRecord> findAll() {
        LOGGER.trace("Get all records");
        return records.values();
    }

    @Override
    public void removeAll() {
        LOGGER.trace("EventRecord remove all records");
        records.clear();
    }
}
