package com.crossent.microservice.finedustback.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FineDustValueItems {

    @JsonProperty("dataTime")
    private String date;

    @JsonProperty("stationName")
    private String stationName;

    @JsonProperty("so2Value")
    private String mvSo2;

    @JsonProperty("coValue")
    private String mvCo;

    @JsonProperty("no2Value")
    private String mvNo2;

    @JsonProperty("o3Value")
    private String mvO3;

    @JsonProperty("pm10Value")
    private String mvPm10;

    @JsonProperty("pm25Value")
    private String mvPm25;

    @JsonProperty("pm10Value24")
    private String pm10Value24;

    @JsonProperty("pm25Value24")
    private String pm25Value24;

}
