package com.crossent.microservice.finedustback.api.controller;

import com.crossent.microservice.finedustback.api.dto.*;
import com.crossent.microservice.finedustback.api.service.FineDustApiService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value="/api/dust")
@RestController
public class FineDustController {

    @Autowired
    private FineDustApiService fineDustApiService;

    @ApiOperation("해당 도의 미세먼지 실시간 측정 데이터 조회 API - 페이징")
    @RequestMapping(value="/measure/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustValueResponse getRealtimeMeasureListWithPaging(@RequestParam(value="sidoName") final String sidoName,
                                                                  @RequestParam(value="pageNo") final Long pageNo,
                                                                  @RequestParam(value="numOfRows") final Long numOfRows){
        return fineDustApiService.getRealtimeMeasureListWithPaging(sidoName, pageNo, numOfRows);
    }

    @ApiOperation("해당 도의 미세먼지 실시간 측정 데이터 조회 API - 전체")
    @RequestMapping(value="/measure/list/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustValueResponse getRealtimeMeasureListAll(@RequestParam(value="sidoName") final String sidoName){

        return fineDustApiService.getRealtimeMeasureListAll(sidoName);
    }

    @ApiOperation("해당 도의 오염물질 별 통합대기환경지수값 조회 API - 페이징")
    @RequestMapping(value="/cai/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustCaiResponse getCaiValueListWithPaging(@RequestParam(value="sidoName") final String sidoName,
                                                         @RequestParam(value="pageNo") final Long pageNo,
                                                         @RequestParam(value="numOfRows") final Long numOfRows){
        return fineDustApiService.getCaiValueListWithPaging(sidoName, pageNo, numOfRows);
    }

    @ApiOperation("해당 도의 오염물질 별 통합대기환경지수값 조회 API - 전체")
    @RequestMapping(value="/cai/list/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustCaiResponse getCaiValueListAll(@RequestParam(value="sidoName") final String sidoName){

        return fineDustApiService.getCaiValueListAll(sidoName);
    }

    @ApiOperation("특정 오염물질의 통합대기환경지수값 조회 API")
    @RequestMapping(value="/cai", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustCaiGrade getSltPollutantCaiValue(@RequestParam(value="pollutant") final String pollutant,
                                                    @RequestParam(value="stationName") final String stationName){

        return fineDustApiService.getSltPollutantCaiValue(pollutant, stationName);
    }

    @ApiOperation("시도별 미세먼지 평균 데이터 조회 API")
    @RequestMapping(value="/measure/avg/sido", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustApiResponse getAvgMeasureListWithSido(@RequestParam(value="pollutant", required = false) final String pollutant){

        return fineDustApiService.getAvgMeasureListWithSido(pollutant);
    }

    @ApiOperation("해당 도, 해당 시간의 시군구별 미세먼지 평균 데이터 조회 API")
    @RequestMapping(value="/measure/avg/time/sigungu", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustApiResponse getSltTimeAvgMeasureList(@RequestParam(value="sidoName") final String sidoName,
                                                  @RequestParam(value="hour") final String hour){

        return fineDustApiService.getSltTimeAvgMeasureList(sidoName, hour);
    }

    @ApiOperation("해당 도의 시군구별 미세먼지 평균 데이터 조회 API")
    @RequestMapping(value="/measure/avg/sigungu", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FineDustApiResponse getAvgMeasureListWithSigungu(@RequestParam(value="sidoName") final String sidoName){

        return fineDustApiService.getAvgMeasureListWithSigungu(sidoName);
    }

}
