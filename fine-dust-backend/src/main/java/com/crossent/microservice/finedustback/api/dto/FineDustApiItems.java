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
public class FineDustApiItems {

    @JsonProperty("dataTime")
    private String date;

    @JsonProperty("stationName")
    private String stationName;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("so2Value")
    private String so2Value;

    @JsonProperty("coValue")
    private String coValue;

    @JsonProperty("no2Value")
    private String no2Value;

    @JsonProperty("o3Value")
    private String o3Value;

    @JsonProperty("pm10Value")
    private String pm10Value;

    @JsonProperty("pm25Value")
    private String pm25Value;

    @JsonProperty("pm10Value24")
    private String pm10Value24;

    @JsonProperty("pm25Value24")
    private String pm25Value24;

    @JsonProperty("khaiValue")
    private String khaiValue;

    @JsonProperty("khaiGrade")
    private String khaiGrade;

    @JsonProperty("so2Grade")
    private String so2Grade;

    @JsonProperty("coGrade")
    private String coGrade;

    @JsonProperty("o3Grade")
    private String o3Grade;

    @JsonProperty("no2Grade")
    private String no2Grade;

    @JsonProperty("pm10Grade")
    private String pm10Grade;

    @JsonProperty("pm25Grade")
    private String pm25Grade;

    @JsonProperty("itemCode")
    private String pollutant;

    @JsonProperty("dataGubun")
    private String searchCondition;

    @JsonProperty("seoul")
    private String seoul;

    @JsonProperty("busan")
    private String busan;

    @JsonProperty("daegu")
    private String daegu;

    @JsonProperty("incheon")
    private String incheon;

    @JsonProperty("gwangju")
    private String gwangju;

    @JsonProperty("daejeon")
    private String daejeon;

    @JsonProperty("ulsan")
    private String ulsan;

    @JsonProperty("gyeonggi")
    private String gyeonggi;

    @JsonProperty("gangwon")
    private String gangwon;

    @JsonProperty("chungbuk")
    private String chungbuk;

    @JsonProperty("chungnam")
    private String chungnam;

    @JsonProperty("jeonbuk")
    private String jeonbuk;

    @JsonProperty("jeonnam")
    private String jeonnam;

    @JsonProperty("gyeongbuk")
    private String gyeongbuk;

    @JsonProperty("gyeongnam")
    private String gyeongnam;

    @JsonProperty("jeju")
    private String jeju;

    @JsonProperty("sejong")
    private String sejong;
}