package com.crossent.msa.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RefreshScope
public class DustMeasureService {
    @Autowired
    RequestService requestService;

    //private String basicUri = "http://localhost:8091";
    private String basicUri = "http://apigateway/fine-dust-service";

    public String sidoIdChange(String englishSido) {
        if (englishSido.equals("busan")) {
            return "부산";
        } else if (englishSido.equals("daegu")) {
            return "대구";
        } else if (englishSido.equals("incheon")) {
            return "인천";
        } else if (englishSido.equals("gwangju")) {
            return "광주";
        } else if (englishSido.equals("daejeon")) {
            return "대전";
        } else if (englishSido.equals("ulsan")) {
            return "울산";
        } else if (englishSido.equals("sejong")) {
            return "세종";
        } else if (englishSido.equals("gyeonggi")) {
            return "경기";
        } else if (englishSido.equals("gangwon")) {
            return "강원";
        } else if (englishSido.equals("chungbuk")) {
            return "충북";
        } else if (englishSido.equals("chungnam")) {
            return "충남";
        } else if (englishSido.equals("jeonbuk")) {
            return "전북";
        } else if (englishSido.equals("jeonnam")) {
            return "전남";
        } else if (englishSido.equals("gyeongbuk")) {
            return "경북";
        } else if (englishSido.equals("gyeongnam")) {
            return "경남";
        } else if (englishSido.equals("jeju")) {
            return "제주";
        } else {
            return "서울";
        }
    }

    private URI setURI(String uri, MultiValueMap<String, String> maps) {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(maps)
                .build()
                .encode()
                .toUri();
    }
    // 1. @ApiOperation("해당 도의 미세먼지 실시간 측정 데이터 조회 API - 페이징")
    public Object getRealtimeMeasureListWithPaging(String sidoName, Long pageNo, Long numOfRows) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("sidoName", sidoName);
        maps.add("pageNo", pageNo.toString());
        maps.add("numOfRows", numOfRows.toString());

        URI uri = setURI(basicUri + "/api/dust/measure/list", maps);
        return requestService.getHttpAsUri(uri);
    }
    // 2. @ApiOperation("해당 도의 미세먼지 실시간 측정 데이터 조회 API - 전체")
    public Object getRealtimeMeasureListAll(String sidoName) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("sidoName", sidoName);

        URI uri = setURI(basicUri + "/api/dust/measure/list/all", maps);
        return requestService.getHttpAsUri(uri);
    }

    // 3. @ApiOperation("해당 도의 오염물질 별 통합대기환경지수값 조회 API - 페이징")
    public Object getCaiValueListWithPaging(String sidoName, Long pageNo, Long numOfRows) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("sidoName", sidoName);
        maps.add("pageNo", pageNo.toString());
        maps.add("numOfRows", numOfRows.toString());

        URI uri = setURI(basicUri + "/api/dust/cai/list", maps);
        return requestService.getHttpAsUri(uri);
    }

    // 4. @ApiOperation("해당 도의 오염물질 별 통합대기환경지수값 조회 API - 전체")
    public Object getCaiValueListAll(String sidoName) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("sidoName", sidoName);
        URI uri = setURI(basicUri + "/api/dust/cai/list/all", maps);
        return requestService.getHttpAsUri(uri);
    }

    // 5. @ApiOperation("특정 오염물질의 통합대기환경지수값 조회 API")

    public Object getSltPollutantCaiValue(String pollutant, String stationName) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("pollutant", pollutant);
        maps.add("stationName", stationName);

        URI uri = setURI(basicUri + "/api/dust/cai", maps);
        return requestService.getHttpAsUri(uri);
    }

    // 6. @ApiOperation("시도별 미세먼지 평균 데이터 조회 API")
    public Object getAvgMeasureListWithSido(String pollutant) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("pollutant", pollutant);
        URI uri = setURI(basicUri + "/api/dust/measure/avg/sido", maps);
        return requestService.getHttpAsUri(uri);
    }

    // 7. @ApiOperation("해당 도, 해당 시간의 시군구별 미세먼지 평균 데이터 조회 API")
    public Object getSltTimeAvgMeasureList(String sidoName, String hour) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("sidoName", sidoName);
        maps.add("hour", hour);
        URI uri = setURI(basicUri + "/api/dust/measure/avg/time/sigungu", maps);
        return requestService.getHttpAsUri(uri);
    }

    // 8. @ApiOperation("해당 도의 시군구별 미세먼지 평균 데이터 조회 API")
    public Object getAvgMeasureListWithSigungu(String sidoName) {
        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
        maps.add("sidoName", sidoName);

        URI uri = setURI(basicUri + "/api/dust/measure/avg/sigungu", maps);
        return requestService.getHttpAsUri(uri);
    }
}
