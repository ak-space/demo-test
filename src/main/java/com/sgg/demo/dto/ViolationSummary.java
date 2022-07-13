package com.sgg.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ViolationSummary {
    private Integer paid;
    private Integer notPaid;
    private Integer total;

}
