package com.crossent.microservice.finedustback.api.service;

import com.crossent.microservice.finedustback.TestConfiguration;
import com.crossent.microservice.finedustback.api.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class FineDustApiServiceTest {
    private final Logger logger = LoggerFactory.getLogger(FineDustApiServiceTest.class);

    MockMvc mockMvc;

    @InjectMocks
    private FineDustApiService fineDustApiService;

    @Mock
    private RequestService requestService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(fineDustApiService).build();
    }

    /**
     * 해당 도의 미세먼지 실시간 측정 데이터 조회 API - 페이징
     * /api/dust/measure/list?sidoName=&pageNo=&numOfRows=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getRealtimeMeasureListWithPaging() throws Exception {

        when(requestService.getHttpMeasureSido(TestConfiguration.SIDO_NAME, String.valueOf(TestConfiguration.PAGE_NO),
                String.valueOf(TestConfiguration.NUM_OF_ROWS))).thenReturn(TestConfiguration.CONTENTS);

        FineDustValueResponse res = fineDustApiService.getRealtimeMeasureListWithPaging(TestConfiguration.SIDO_NAME,
                TestConfiguration.PAGE_NO, TestConfiguration.NUM_OF_ROWS);

        assertFalse(res == null);
        logger.info(res.toString());
        logger.debug("========================="+res.toString());

    }

    /**
     * ---------내부 호출 문제 해결해야함---------
     * 해당 도의 미세먼지 실시간 측정 데이터 조회 API - 전체
     * /api/dust/measure/list/all?sidoName=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getRealtimeMeasureListAll() throws Exception {

        when(requestService.getHttpMeasureSido(TestConfiguration.SIDO_NAME, String.valueOf(-1),
                String.valueOf(-1))).thenReturn(TestConfiguration.CONTENTS);
        FineDustValueResponse res = fineDustApiService.getRealtimeMeasureListAll(TestConfiguration.SIDO_NAME);
        assertFalse(res == null);
        logger.info(res.toString());
        logger.debug("========================="+res.toString());

    }

    /**
     * 해당 도의 오염물질 별 통합대기환경지수값 조회 API - 페이징
     * /api/dust/cai/list?sidoName=&pageNo=&numOfRows=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getCaiValueListWithPaging() throws Exception {

        when(requestService.getHttpMeasureSido(TestConfiguration.SIDO_NAME, String.valueOf(TestConfiguration.PAGE_NO),
                String.valueOf(TestConfiguration.NUM_OF_ROWS))).thenReturn(TestConfiguration.CONTENTS);

        FineDustCaiResponse res = fineDustApiService.getCaiValueListWithPaging(TestConfiguration.SIDO_NAME, TestConfiguration.PAGE_NO,
                TestConfiguration.NUM_OF_ROWS);
        assertFalse(res == null);
        logger.debug("========================="+res.toString());
        logger.info(res.toString());

    }

    /**
     * ---------메소드 내부 호출----------
     * 해당 도의 오염물질 별 통합대기환경지수값 조회 API - 전체
     * /api/dust/cai/list/all?sidoName=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getCaiValueListAll() throws Exception {

        when(requestService.getHttpMeasureSido(TestConfiguration.SIDO_NAME, String.valueOf(-1),
                String.valueOf(-1))).thenReturn(TestConfiguration.CONTENTS);

        FineDustCaiResponse res = fineDustApiService.getCaiValueListAll(TestConfiguration.SIDO_NAME);
        assertFalse(res == null);
        logger.debug("========================="+res.toString());
        logger.info(res.toString());

    }

    /**
     * 특정 오염물질의 통합대기환경지수값 조회 API
     * /api/dust/cai?pollutant=&stationName=
     * return FineDustCaiGrade
     * @throws Exception
     */
    @Test
    public void getSltPollutantCaiValue() throws Exception {

        when(requestService.getHttpMeasureStation(TestConfiguration.STATION_NAME, "",""))
                .thenReturn(TestConfiguration.CONTENTS);

        FineDustCaiGrade grade = fineDustApiService.getSltPollutantCaiValue(TestConfiguration.POLLUTANT, TestConfiguration.STATION_NAME);
        assertFalse(grade == null);
        logger.info(grade.toString());
        logger.debug("========================="+grade.toString());

    }

    /**
     * 시도별 미세먼지 평균 데이터 조회 API
     * /api/dust/measure/avg/sido?pollutant=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getAvgMeasureListWithSido() throws Exception {

        when(requestService.getHttpMeasureAvgSidoApi(TestConfiguration.POLLUTANT)).thenReturn(TestConfiguration.CONTENTS);
        FineDustApiResponse res = fineDustApiService.getAvgMeasureListWithSido(TestConfiguration.POLLUTANT);

        assertFalse(res == null);
        logger.info(res.toString());
        logger.debug("========================="+res.toString());

    }

    /**
     * 해당 도, 해당 시간의 시군구별 미세먼지 평균 데이터 조회 API
     * /api/dust/measure/avg/time/sigungu?sidoName=&hour=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getSltTimeAvgMeasureList() throws Exception {

        when(requestService.getHttpMeasureAvgSigungu(TestConfiguration.SIDO_NAME)).thenReturn(TestConfiguration.CONTENTS);
        FineDustApiResponse res = fineDustApiService.getSltTimeAvgMeasureList(TestConfiguration.SIDO_NAME, TestConfiguration.HOUR);
        assertFalse(res == null);
        logger.info(res.toString());
        logger.debug(res.toString());

    }

    /**
     * 해당 도의 시군구별 미세먼지 평균 데이터 조회 API
     * /api/dust/measure/avg/sigungu?sidoName=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getAvgMeasureListWithSigungu() throws Exception {

        when(requestService.getHttpMeasureAvgSigungu(TestConfiguration.SIDO_NAME)).thenReturn(TestConfiguration.CONTENTS);
        FineDustApiResponse res = fineDustApiService.getAvgMeasureListWithSigungu(TestConfiguration.SIDO_NAME);
        assertFalse(res == null);
        logger.info(res.toString());
        logger.debug("========================="+res.toString());

    }

}
