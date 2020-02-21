package com.crossent.msa.example;

import com.crossent.msa.example.dto.DustAverageSido;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping(value="/api/dust")
public class DustMeasureController {
    @Autowired
    DustMeasureService dustMeasureService;

    @ApiOperation("해당 도의 미세먼지 실시간 측정 데이터 조회 API - 페이징")
    @RequestMapping(value="/measure/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object getRealtimeMeasureListWithPaging(@RequestParam(value="sidoName") final String sidoName,
                                                             @RequestParam(value="pageNo") final Long pageNo,
                                                             @RequestParam(value="numOfRows") final Long numOfRows){
        return dustMeasureService.getRealtimeMeasureListWithPaging(sidoName, pageNo, numOfRows);
    }

    @ApiOperation("해당 도의 미세먼지 실시간 측정 데이터 조회 API - 전체")
    @RequestMapping(value="/measure/list/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object getRealtimeMeasureListAll(@RequestParam(value="sidoName") final String sidoName){

        return dustMeasureService.getRealtimeMeasureListAll(sidoName);
    }

    @ApiOperation("해당 도의 오염물질 별 통합대기환경지수값 조회 API - 페이징")
    @RequestMapping(value="/cai/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object getCaiValueListWithPaging(@RequestParam(value="sidoName") final String sidoName,
                                                      @RequestParam(value="pageNo") final Long pageNo,
                                                      @RequestParam(value="numOfRows") final Long numOfRows){
        return dustMeasureService.getCaiValueListWithPaging(sidoName, pageNo, numOfRows);
    }

    @ApiOperation("해당 도의 오염물질 별 통합대기환경지수값 조회 API - 전체")
    @RequestMapping(value="/cai/list/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object dustCaiListAll(@RequestParam(name="sidoName", required = false)String sido){
        return dustMeasureService.getCaiValueListAll(sido);
    }

    @ApiOperation("특정 오염물질의 통합대기환경지수값 조회 API")
    @RequestMapping(value="/cai", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object getSltPollutantCaiValue(@RequestParam(value="pollutant") final String pollutant,
                                                    @RequestParam(value="stationName") final String stationName){

        return dustMeasureService.getSltPollutantCaiValue(pollutant, stationName);
    }

    @ApiOperation("시도별 미세먼지 평균 데이터 조회 API")
    @RequestMapping(value="/measure/avg/sido", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object dustAverageSido(@RequestParam(value="pollutant", required = false) String pollutant) {
        //pollutant="aa";
        return dustMeasureService.getAvgMeasureListWithSido(pollutant);
    }

    @ApiOperation("해당 도, 해당 시간의 시군구별 미세먼지 평균 데이터 조회 API")
    @RequestMapping(value="/measure/avg/time/sigungu", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object dustAvgTimeSigungu(@RequestParam(name="sidoName" , required = false)String sido,
                                     @RequestParam(name="hour") String hour ){
        return dustMeasureService.getSltTimeAvgMeasureList(sido, hour);
    }

    @ApiOperation("해당 도의 시군구별 미세먼지 평균 데이터 조회 API")
    @RequestMapping(value="/measure/avg/sigungu", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Object getAvgMeasureListWithSigungu(@RequestParam(value="sidoName") final String sidoName){

        return dustMeasureService.getAvgMeasureListWithSigungu(sidoName);
    }
}
