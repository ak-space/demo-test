package com.sgg.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.service.EventRecordService;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EventRecordControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRecordService eventRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addEventRecord_whenCreateEventRecord_thenReturnSavedEventRecord() throws Exception {

        EventRecord eventRecord = EventRecord.builder()
                .eventType("TEST")
                .licensePlate("TestLicensePlate")
                .speed(40)
                .limit(50)
                .unity("km/h")
                .build();

        eventRecordService.removeAll();
        ResultActions response = mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRecord)));


        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(notNullValue())))
                .andExpect(jsonPath("eventDate", is(notNullValue())))
                .andExpect(jsonPath("speed", is(eventRecord.getSpeed())))
                .andExpect(jsonPath("unity", is(eventRecord.getUnity())))
                .andExpect(jsonPath("eventType", is(eventRecord.getEventType())))
                .andExpect(jsonPath("limit", is(eventRecord.getLimit())))
                .andExpect(jsonPath("licensePlate", is(eventRecord.getLicensePlate())))
                .andExpect(jsonPath("processed", anything()));

    }

    @Test
    public void addEventRecord_whenGetAll_thenReturnEventRecordsList() throws Exception {

        List<EventRecord> eventRecords = List.of(
                EventRecord.builder()
                        .eventType("TEST1").licensePlate("TestLicensePlate1").speed(40).limit(60).unity("km/h").processed(false).build(),
                EventRecord.builder()
                        .eventType("TEST2").licensePlate("TestLicensePlate2").speed(60).limit(80).unity("km/h").processed(false).build());


        eventRecordService.removeAll();
        eventRecordService.save(eventRecords.get(0));
        eventRecordService.save(eventRecords.get(1));

        ResultActions response = mockMvc.perform(get("/records"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(eventRecords.size())));

    }

    @Test
    public void addEventRecord_whenCreateEventRecord_thenCheckProcessedEvents() throws Exception {

        EventRecord eventRecords1 = EventRecord.builder()
                .eventType("SPEED").licensePlate("TestLicensePlate-01").speed(100).limit(80).unity("km/h").processed(false).build();
        EventRecord eventRecords2 = EventRecord.builder()
                .eventType("SPEED").licensePlate("TestLicensePlate-02").speed(170).limit(50).unity("km/h").processed(false).build();

        eventRecordService.removeAll();
        mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRecords1))).andReturn();
        mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRecords2))).andReturn();

        Awaitility.await().atMost(5, TimeUnit.MILLISECONDS);
        ResultActions resultActions = mockMvc.perform(get("/records"));
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].processed", is(true)))
                .andExpect(jsonPath("$[1].processed", is(true)));

    }
}
