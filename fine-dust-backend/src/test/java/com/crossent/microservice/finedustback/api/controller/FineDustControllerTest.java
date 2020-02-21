package com.crossent.microservice.finedustback.api.controller;

import com.crossent.microservice.finedustback.FinedustBackApplication;
import com.crossent.microservice.finedustback.TestConfiguration;
import com.crossent.microservice.finedustback.api.service.FineDustApiService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FinedustBackApplication.class, properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.config.discovery.enabled = false",
        "spring.cloud.config.enabled = false"
})
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class FineDustControllerTest {

    private final Logger logger = LoggerFactory.getLogger(FineDustControllerTest.class);

    MockMvc mockMvc;

    @InjectMocks
    private FineDustController fineDustController;

    @Mock
    private FineDustApiService fineDustApiService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(fineDustController).build();
    }

    /**
     * 해당 도의 미세먼지 실시간 측정 데이터 조회 API - 페이징
     * /api/dust/measure/list?sidoName=&pageNo=&numOfRows=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getRealtimeMeasureListWithPaging() throws Exception {

        when(fineDustApiService.getRealtimeMeasureListWithPaging(TestConfiguration.SIDO_NAME,TestConfiguration.PAGE_NO,
                TestConfiguration.NUM_OF_ROWS)).thenReturn(TestConfiguration.getFineDustValueResponse());

        this.mockMvc.perform(get("/api/dust/measure/list")
                .param("sidoName",TestConfiguration.SIDO_NAME)
                .param("pageNo",String.valueOf(TestConfiguration.PAGE_NO))
                .param("numOfRows",String.valueOf(TestConfiguration.NUM_OF_ROWS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andReturn();

    }

    /**
     * 해당 도의 미세먼지 실시간 측정 데이터 조회 API - 전체
     * /api/dust/measure/list/all?sidoName=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getRealtimeMeasureListAll() throws Exception {

        when(fineDustApiService.getRealtimeMeasureListAll(TestConfiguration.SIDO_NAME)).thenReturn(TestConfiguration.getFineDustValueResponse());

        this.mockMvc.perform(get("/api/dust/measure/list/all")
                .param("sidoName",TestConfiguration.SIDO_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andReturn();

    }

    /**
     * 해당 도의 오염물질 별 통합대기환경지수값 조회 API - 페이징
     * /api/dust/cai/list?sidoName=&pageNo=&numOfRows=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getCaiValueListWithPaging() throws Exception {

        when(fineDustApiService.getCaiValueListWithPaging(TestConfiguration.SIDO_NAME, TestConfiguration.PAGE_NO,
                TestConfiguration.NUM_OF_ROWS)).thenReturn(TestConfiguration.getFineDustCaiResponse());

        this.mockMvc.perform(get("/api/dust/cai/list")
                .param("sidoName",TestConfiguration.SIDO_NAME)
                .param("pageNo",String.valueOf(TestConfiguration.PAGE_NO))
                .param("numOfRows",String.valueOf(TestConfiguration.NUM_OF_ROWS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print())
                .andReturn();

    }

    /**
     * 해당 도의 오염물질 별 통합대기환경지수값 조회 API - 전체
     * /api/dust/cai/list/all?sidoName=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getCaiValueListAll() throws Exception {

        when(fineDustApiService.getCaiValueListAll(TestConfiguration.SIDO_NAME)).thenReturn(TestConfiguration.getFineDustCaiResponse());
        this.mockMvc.perform(get("/api/dust/cai/list/all")
                .param("sidoName",TestConfiguration.SIDO_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print())
                .andReturn();

    }

    /**
     * 특정 오염물질의 통합대기환경지수값 조회 API
     * /api/dust/cai?pollutant=&stationName=
     * return FineDustCaiGrade
     * @throws Exception
     */
    @Test
    public void getSltPollutantCaiValue() throws Exception {

        when(fineDustApiService.getSltPollutantCaiValue(TestConfiguration.POLLUTANT, TestConfiguration.STATION_NAME))
                .thenReturn(TestConfiguration.getFineDustCaiGrade());
        this.mockMvc.perform(get("/api/dust/cai")
                .param("pollutant", TestConfiguration.POLLUTANT)
                .param("stationName", TestConfiguration.STATION_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print())
                .andReturn();

    }

    /**
     * 시도별 미세먼지 평균 데이터 조회 API
     * /api/dust/measure/avg/sido?pollutant=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getAvgMeasureListWithSido() throws Exception {

        when(fineDustApiService.getAvgMeasureListWithSido(TestConfiguration.POLLUTANT)).thenReturn(TestConfiguration.getMeasureAvgSidoData());
        this.mockMvc.perform(get("/api/dust/measure/avg/sido")
                .param("pollutant",TestConfiguration.POLLUTANT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print())
                .andReturn();

    }

    /**
     * 해당 도, 해당 시간의 시군구별 미세먼지 평균 데이터 조회 API
     * /api/dust/measure/avg/time/sigungu?sidoName=&hour=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getSltTimeAvgMeasureList() throws Exception {

        when(fineDustApiService.getSltTimeAvgMeasureList(TestConfiguration.SIDO_NAME, TestConfiguration.HOUR))
                .thenReturn(TestConfiguration.getFineDustApiResponse());

        this.mockMvc.perform(get("/api/dust/measure/avg/time/sigungu")
                .param("sidoName",TestConfiguration.SIDO_NAME)
                .param("hour",TestConfiguration.HOUR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print())
                .andReturn();

    }

    /**
     * 해당 도의 시군구별 미세먼지 평균 데이터 조회 API
     * /api/dust/measure/avg/sigungu?sidoName=
     * return FineDustResponse
     * @throws Exception
     */
    @Test
    public void getAvgMeasureListWithSigungu() throws Exception {

        when(fineDustApiService.getAvgMeasureListWithSigungu(TestConfiguration.SIDO_NAME)).thenReturn(TestConfiguration.getFineDustApiResponse());
        this.mockMvc.perform(get("/api/dust/measure/avg/sigungu")
                .param("sidoName",TestConfiguration.SIDO_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print())
                .andReturn();

    }


}
