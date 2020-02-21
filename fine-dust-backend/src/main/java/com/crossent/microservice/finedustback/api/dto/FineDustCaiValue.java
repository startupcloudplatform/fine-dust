package com.crossent.microservice.finedustback.api.dto;

import com.crossent.microservice.finedustback.api.dto.FineDustCaiGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FineDustCaiValue {

    private String stationName;
    private String date;
    private String cvSo2;
    private String cvCo;
    private String cvO3;
    private String cvNo2;
    private String cvPm10;
    private String cvPm25;
    private String khaiValue;
    private List<FineDustCaiGrade> grades;

}
