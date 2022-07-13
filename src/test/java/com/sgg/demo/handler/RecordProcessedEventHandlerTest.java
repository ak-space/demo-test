package com.sgg.demo.handler;

import com.sgg.demo.BaseTestHelper;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.event.RecordProcessedEvent;
import com.sgg.demo.exception.EntityNotFoundException;
import com.sgg.demo.service.EventRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecordProcessedEventHandlerTest extends BaseTestHelper {

    @Mock
    private EventRecordService eventRecordService;

    @InjectMocks
    private RecordProcessedEventHandler recordProcessedEventHandler;

    @Test
    public void handle_checkEventRecord_eventRecordUpdated() throws EntityNotFoundException {

        EventRecord eventRecord = EventRecord.builder()
                .id(ID)
                .processed(false)
                .eventType("TEST")
                .build();

        EventRecord expectedEventRecord = EventRecord.builder()
                .id(ID)
                .eventType("TEST")
                .processed(true)
                .build();

        when(eventRecordService.get(ID)).thenReturn(eventRecord);
        recordProcessedEventHandler.handle(new RecordProcessedEvent(ID));
        verify(eventRecordService).get(ID);
        verify(eventRecordService).update(expectedEventRecord);
    }

    @Test
    public void handle_checkEventRecord_logErrorMessage() throws EntityNotFoundException {

        EventRecord eventRecord = EventRecord.builder()
                .id(ID)
                .processed(false)
                .eventType("TEST")
                .build();

        EventRecord expectedEventRecord = EventRecord.builder()
                .id(ID)
                .eventType("TEST")
                .processed(true)
                .build();

        when(eventRecordService.get(ID)).thenReturn(eventRecord);
        doThrow(new EntityNotFoundException()).when(eventRecordService).update(expectedEventRecord);
        recordProcessedEventHandler.handle(new RecordProcessedEvent(ID));
        verify(eventRecordService).update(expectedEventRecord);

    }

}
