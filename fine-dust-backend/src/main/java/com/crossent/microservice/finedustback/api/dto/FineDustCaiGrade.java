package com.crossent.microservice.finedustback.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FineDustCaiGrade {
    private String name;
    private String value;
    private String grade;
}
