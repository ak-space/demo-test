package com.sgg.demo.controller;

import com.sgg.demo.controller.request.CreateRecordRequest;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.service.EventRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/records")
public class EventRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRecordController.class);
    private final EventRecordService eventRecordService;

    @Autowired
    public EventRecordController(EventRecordService eventRecordService) {
        this.eventRecordService = eventRecordService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventRecord> addEvent(@RequestBody CreateRecordRequest recordRequest) {

        LOGGER.trace("Record API apply record");
        EventRecord eventRecord = EventRecord.builder()
                .eventType(recordRequest.getEventType())
                .unity(recordRequest.getUnity())
                .limit(recordRequest.getLimit())
                .speed(recordRequest.getSpeed())
                .licensePlate(recordRequest.getLicensePlate())
                .build();

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventRecordService.save(eventRecord));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventRecord>> getRecords() {

        LOGGER.trace("Get records");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventRecordService.findAll());
    }
}
