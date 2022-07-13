package com.sgg.demo.service.strategy;

import com.sgg.demo.BaseTestHelper;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.service.ViolationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpeedStrategyTest extends BaseTestHelper {
    @Mock
    private ViolationService violationService;

    @InjectMocks
    private SpeedStrategy speedStrategy;

    @Test
    public void check_eventRecordPresent_violationNotCreated() {

        EventRecord eventRecord = EventRecord.builder()
                .id(ID)
                .eventType("TEST")
                .licensePlate("TestLicensePlate")
                .speed(40)
                .limit(50)
                .unity("km/h")
                .processed(false)
                .eventDate(CONST_DATE)
                .build();


        speedStrategy.check(eventRecord);
        Mockito.verify(violationService, never()).create(any(), anyDouble());
    }

    @Test
    public void check_eventRecordPresent_violationCreated() {

        EventRecord eventRecord = EventRecord.builder()
                .id(ID)
                .eventType("TEST")
                .licensePlate("TestLicensePlate")
                .speed(150)
                .limit(50)
                .unity("km/h")
                .processed(false)
                .eventDate(CONST_DATE)
                .build();

        speedStrategy.check(eventRecord);
        Mockito.verify(violationService, only()).create(any(), anyDouble());
    }

    @Test
    public void check_eventRecordNotPresent_violationNotCreated() {

        speedStrategy.check(null);
        Mockito.verify(violationService, never()).create(any(), anyDouble());
    }


}
