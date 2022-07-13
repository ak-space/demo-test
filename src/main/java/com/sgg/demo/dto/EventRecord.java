package com.sgg.demo.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

//            "id": "d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
//            "eventDate": "2022-02-09T00:25:20.529",
//            "eventType": "SPEED",
//            "licensePlate": "ABC-123",
//            "speed": 87,
//            "limit": 50,
//            "unity": "km/h",
//            "processed": "false"
@Getter
@Setter
@Builder(toBuilder=true)
@ToString
@EqualsAndHashCode
public class EventRecord {
    private UUID id;
    private LocalDateTime eventDate;
    private String eventType;
    private String licensePlate;
    private Integer speed;
    private Integer limit;
    private String unity;
    @Builder.Default
    private Boolean processed = false;
}
