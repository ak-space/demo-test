package com.sgg.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sgg.demo.dto.EventRecord;
import com.sgg.demo.service.ViolationService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ViolationControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ViolationService violationService;

    @BeforeEach
    public void init() {

    }

    @Test
    public void payViolation_whenCreateEventRecordWithViolation_thenViolationMarkAsPaid() throws Exception {

        List<EventRecord> eventRecords = List.of(
                EventRecord.builder()
                        .eventType("SPEED").licensePlate("TestLicensePlate-01").speed(100).limit(80).unity("km/h").processed(false).build(),
                EventRecord.builder()
                        .eventType("SPEED").licensePlate("TestLicensePlate-02").speed(170).limit(50).unity("km/h").processed(false).build());


        addEventRecord(eventRecords.get(0));
        addEventRecord(eventRecords.get(1));


        ResultActions violationResponse = mockMvc.perform(get("/violations"));
        violationResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(eventRecords.size())));

        MvcResult mvcResult = mockMvc.perform(get("/violations")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$[0].id");

        mockMvc.perform(put("/violations/{id}/pay",id))
                .andDo(print())
                .andExpect(jsonPath("id", equalTo(id)))
                .andExpect(jsonPath("paid", equalTo(true)));

    }

    @Test
    public void violationSummary_whenCreateEventRecordWithViolation_thenReturnSummary() throws Exception {

        List<EventRecord> eventRecords = List.of(
                EventRecord.builder()
                        .eventType("SPEED").licensePlate("TestLicensePlate-01").speed(100).limit(80).unity("km/h").processed(false).build(),
                EventRecord.builder()
                        .eventType("NEW_EVENT").licensePlate("TestLicensePlate-02").speed(100).limit(80).unity("km/h").processed(false).build(),
                EventRecord.builder()
                        .eventType("REDLIGHT").licensePlate("TestLicensePlate-03").speed(50).limit(60).unity("km/h").processed(false).build(),
                EventRecord.builder()
                        .eventType("SPEED").licensePlate("TestLicensePlate-04").speed(170).limit(50).unity("km/h").processed(false).build());

        violationService.removeAll();
        addEventRecord(eventRecords.get(0));
        addEventRecord(eventRecords.get(1));
        addEventRecord(eventRecords.get(2));
        addEventRecord(eventRecords.get(3));

        MvcResult mvcResult = mockMvc.perform(get("/violations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$[0].id");

        mockMvc.perform(put("/violations/{id}/pay",id))
                .andDo(print())
                .andExpect(jsonPath("id", equalTo(id)))
                .andExpect(jsonPath("paid", equalTo(true)));

        ResultActions violationResponse = mockMvc.perform(get("/violations/summary"));
        violationResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("paid",is(1)))
                .andExpect(jsonPath("total",is(3)));

    }

    @Test
    public void addEventRecord_whenCreateEventRecord_thenEmptyViolationLIst() throws Exception {

        EventRecord eventRecords1 = EventRecord.builder()
                .eventType("NEW_TYPE").licensePlate("TestLicensePlate-01").speed(100).limit(80).unity("km/h").processed(false).build();

        violationService.removeAll();
        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRecords1)))
                .andDo(print()).
                andExpect(status().isCreated());

        mockMvc.perform(get("/violations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(0)));

    }

    private void addEventRecord(EventRecord eventRecord) throws Exception {
        mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRecord))).andReturn();
    }

}
