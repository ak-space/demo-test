package com.sgg.demo.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecordRequest {
    private LocalDateTime eventDate;
    private String eventType;
    private String licensePlate;
    private Integer speed;
    private Integer limit;
    private String unity;
}
