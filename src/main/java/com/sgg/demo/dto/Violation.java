package com.sgg.demo.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

//"id": "b11e87f0-89ef-11ec-a8a3-0242ac120002",
//        "eventId": "d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
//        "fine": 50,
//        "paid": "false"
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Violation {

    private UUID id;
    private UUID eventId;
    private Double fine;
    @Builder.Default
    private Boolean paid = false;
}
