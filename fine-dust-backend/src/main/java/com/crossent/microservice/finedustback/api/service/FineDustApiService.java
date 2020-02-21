package com.crossent.microservice.finedustback.api.service;

import com.crossent.microservice.finedustback.api.dto.*;
import com.crossent.microservice.finedustback.api.dto.FineDustCaiGrade;
import com.crossent.microservice.finedustback.api.dto.FineDustCaiValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.XML;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RefreshScope
public class FineDustApiService {

    private static final Logger logger = LoggerFactory.getLogger(FineDustApiService.class);
    private static final String API_ERROR = "OpenApi Response Error";
    private static final String TIMEOUT_ERROR = "Timeout Error";
    private static final String MANDATORY_VALUE_ERROR = "Mandatory value";
    private static final String NO_RESULT_ERROR = "No Result";

    @Value("${openData.market.measure.sido.api:}")
    private String measureSidoApi;

    @Value("${openData.market.measure.station.api}")
    private String measureStationApi;

    @Value("${openData.market.measure.avg.sigungu.api}")
    private String measureAvgSigunguApi;

    @Value("${openData.market.measure.avg.sido.api}")
    private String measureAvgSidoApi;

    @Value("${openData.market.authKey:}")
    private String authKey;

    @Autowired
    private RequestService requestService;

    /**
     * 해당 도의 미세먼지 실시간 측정 데이터 조회 API - 페이징
     * @param sidoName
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public FineDustValueResponse getRealtimeMeasureListWithPaging(String sidoName, Long pageNo, Long numOfRows){

        FineDustValueResponse res = new FineDustValueResponse();
        // 비어있는 필수 파라미터 값 처리
        if(sidoName.equals("")){
            FineDustValueItems vi =  new FineDustValueItems();
            vi.setStationName("sidoName is "+MANDATORY_VALUE_ERROR);
            res.getItems().add(vi);
            return res;
        }

        String contents = requestService.getHttpMeasureSido(sidoName, String.valueOf(pageNo), String.valueOf(numOfRows));
        // OpenApi 응답 에러 처리
        if(contents.equals(API_ERROR) || contents.equals(TIMEOUT_ERROR)){
            FineDustValueItems vi =  new FineDustValueItems();
            vi.setStationName(contents);
            res.getItems().add(vi);
            return res;
        }

        FineDustBody body = getBodyResponse(contents);
        List<FineDustApiItems> apiItems = getItemsResponse(contents);
        List<FineDustValueItems> valueItems = convertApiItemToValueItem(apiItems);
        res.setBody(body);
        res.setItems(valueItems);
        return res;
    }

    /**
     * 해당 도의 미세먼지 실시간 측정 데이터 조회 API - 전체
     * @param sidoName
     * @return
     */
    public FineDustValueResponse getRealtimeMeasureListAll(String sidoName){
        return getRealtimeMeasureListWithPaging(sidoName, Long.valueOf(-1),Long.valueOf(-1));
    }

    /**
     * 시도별 실시간 평균정보 조회 API
     * @param pollutant
     * @return
     */
    public FineDustApiResponse getAvgMeasureListWithSido(String pollutant){

        FineDustApiResponse res = new FineDustApiResponse();
        // 비어있는 필수 파라미터 값 처리
        if(pollutant.equals("")){
            FineDustApiItems ai = new FineDustApiItems();
            ai.setStationName("pollutant is "+MANDATORY_VALUE_ERROR);
            res.getItems().add(ai);
            return res;
        }

        pollutant = pollutant.toLowerCase();
        // 오염물질이 유효하지 않은 경우
        if(!pollutant.equals("so2") && !pollutant.equals("no2") && !pollutant.equals("co") && !pollutant.equals("o2")
                && !pollutant.equals("o3") && !pollutant.equals("pm10") && !pollutant.equals("pm25")){
            FineDustApiItems ai = new FineDustApiItems();
            ai.setStationName("pollutant is wrong value");
            res.getItems().add(ai);
            return res;
        }

        String contents = requestService.getHttpMeasureAvgSidoApi(pollutant);
        // OpenApi 응답 에러 처리
        if(contents.equals(API_ERROR) || contents.equals(TIMEOUT_ERROR)){
            FineDustApiItems ai =  new FineDustApiItems();
            ai.setStationName(contents);
            res.getItems().add(ai);
            return res;
        }

        FineDustBody body = getBodyResponse(contents);
        List<FineDustApiItems> items = getItemsResponse(contents);
        res.setBody(body);
        res.setItems(items);
        return res;
    }

    /**
     * 해당 도, 해당 시간의 미세먼지 평균 데이터 조회 API
     * @param sidoName
     * @param hour
     * @return
     */
    public FineDustApiResponse getSltTimeAvgMeasureList(String sidoName, String hour){

        FineDustApiResponse res = new FineDustApiResponse();
        // 비어있는 필수 파라미터 값 처리
        if(sidoName.equals("")){
            FineDustApiItems ai =  new FineDustApiItems();
            ai.setStationName("sidoName is "+MANDATORY_VALUE_ERROR);
            res.getItems().add(ai);
            return res;
        }

        String contents = requestService.getHttpMeasureAvgSigungu(sidoName);
        // OpenApi 응답 에러 처리
        if(contents.equals(API_ERROR) || contents.equals(TIMEOUT_ERROR)){
            FineDustApiItems ai =  new FineDustApiItems();
            ai.setStationName(contents);
            res.getItems().add(ai);
            return res;
        }

        FineDustBody body = getBodyResponse(contents);
        List<FineDustApiItems> apiItems = getItemsResponse(contents);
        //결과값 없을 경우
        if(apiItems == null){
            FineDustApiItems ai =  new FineDustApiItems();
            ai.setStationName(NO_RESULT_ERROR);
            res.getItems().add(ai);
            return res;
        }

        //오늘 날짜 추출
        String searchDate = LocalDate.now().toString()+" "+hour;
        for(int i=0;i<apiItems.size();i++){
            if(!(apiItems.get(i).getDate().contains(searchDate))){
                apiItems.remove(i);
                i--;
            }
        }

        res.setBody(body);
        res.setItems(apiItems);
        return res;
    }

    /**
     * 해당 도의 시군구별 미세먼지 평균 데이터 조회 API
     * @param sidoName
     * @return
     */
    public FineDustApiResponse getAvgMeasureListWithSigungu(String sidoName){

        FineDustApiResponse res = new FineDustApiResponse();
        // 비어있는 필수 파라미터 값 처리
        if(sidoName.equals("")){
            FineDustApiItems ai =  new FineDustApiItems();
            ai.setStationName("sidoName is "+MANDATORY_VALUE_ERROR);
            res.getItems().add(ai);
            return res;
        }

        String contents = requestService.getHttpMeasureAvgSigungu(sidoName);
        // OpenApi 응답 에러 처리
        if(contents.equals(API_ERROR) || contents.equals(TIMEOUT_ERROR)){
            FineDustApiItems ai =  new FineDustApiItems();
            ai.setStationName(contents);
            res.getItems().add(ai);
            return res;
        }

        FineDustBody body = getBodyResponse(contents);
        List<FineDustApiItems> items = getItemsResponse(contents);
        res.setBody(body);
        res.setItems(items);
        return res;
    }

    /**
     * Reponse-body 추출
     * @param contents
     * @return
     */
    public FineDustBody getBodyResponse(String contents){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSONObject jsonToString = XML.toJSONObject(contents);
        FineDustBody bodyResponse = null;
        try{
            JsonNode jsonNodeRoot = mapper.readTree(jsonToString.toString());
            JsonNode headerNode = jsonNodeRoot.get("response").get("header").get("resultCode");
            if(headerNode.asText().equals("00")){
                JsonNode bodyNode = jsonNodeRoot.get("response").get("body");
                bodyResponse = mapper.readValue(bodyNode.toString(), new TypeReference<FineDustBody>(){});
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bodyResponse;
    }

    /**
     * Response-items 추출
     * @param contents
     * @return
     */
    public List<FineDustApiItems> getItemsResponse(String contents){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSONObject jsonToString = XML.toJSONObject(contents);
        List<FineDustApiItems> items = null;

        try{
            JsonNode jsonNodeRoot = mapper.readTree(jsonToString.toString());
            JsonNode headerNode = jsonNodeRoot.get("response").get("header").get("resultCode");
            if(headerNode.asText().equals("00")){
                JsonNode itemNode = jsonNodeRoot.get("response").get("body").get("items").get("item");
                if(itemNode != null){
                    items = mapper.readValue(itemNode.toString(), new TypeReference<ArrayList<FineDustApiItems>>(){});
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Response apiItems 에서 valueItems 로 dto convert
     * @param items
     * @return
     */
    public List<FineDustValueItems> convertApiItemToValueItem(List<FineDustApiItems> items){
        ObjectMapper mapper = new ObjectMapper();
        List<FineDustValueItems> res = null;
        try{
            String itemStr = mapper.writeValueAsString(items);
            res = mapper.readValue(itemStr, new TypeReference<ArrayList<FineDustValueItems>>(){});

        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    // ------------------------------cai 계산 ---------------------------------------//

    /**
     * 해당 도의 오염물질 별 통합대기환경지수값 조회 APi - 페이징
     */
    public FineDustCaiResponse getCaiValueListWithPaging(String sidoName, Long pageNo, Long numOfRows){

        FineDustCaiResponse res = new FineDustCaiResponse();
        // 비어있는 필수 파라미터 값 처리
        if(sidoName.equals("")){
            FineDustCaiValue cv =  new FineDustCaiValue();
            cv.setStationName("sidoName is "+MANDATORY_VALUE_ERROR);
            res.getItems().add(cv);
            return res;
        }

        String contents = requestService.getHttpMeasureSido(sidoName, String.valueOf(pageNo), String.valueOf(numOfRows));
        // OpenApi 응답 에러 처리
        if(contents.equals(API_ERROR) || contents.equals(TIMEOUT_ERROR)){
            FineDustCaiValue cv =  new FineDustCaiValue();
            cv.setStationName(contents);
            res.getItems().add(cv);
            return res;
        }

        FineDustBody body = getBodyResponse(contents);
        List<FineDustApiItems> apiItems = getItemsResponse(contents);

        // 결과값 없을 경우
        if(apiItems == null){
            FineDustCaiValue cv =  new FineDustCaiValue();
            cv.setStationName(NO_RESULT_ERROR);
            res.getItems().add(cv);
            return res;
        }

        // cai지수 리스트 생성
        List<FineDustCaiValue> caiValues = new ArrayList<>();
        for(FineDustApiItems item : apiItems){
            // cai지수 계산
            FineDustCaiValue cai = calculateCaiValue(item);
            if(cai != null){
                // 등급값 셋팅
                List<FineDustCaiGrade> caiGrades = new ArrayList<>();
                caiGrades.add( new FineDustCaiGrade("so2",cai.getCvSo2(), item.getSo2Grade()) );
                caiGrades.add( new FineDustCaiGrade("co",cai.getCvCo(), item.getCoGrade()) );
                caiGrades.add( new FineDustCaiGrade("no2",cai.getCvNo2(), item.getNo2Grade()) );
                caiGrades.add( new FineDustCaiGrade("o3",cai.getCvO3(), item.getSo2Grade()) );
                caiGrades.add( new FineDustCaiGrade("pm10",cai.getCvPm10(), item.getPm10Grade()) );
                caiGrades.add( new FineDustCaiGrade("pm25",cai.getCvPm25(), item.getPm25Grade()) );
                caiGrades.add( new FineDustCaiGrade("khaiValue",cai.getKhaiValue(), item.getKhaiGrade()) );
                cai.setGrades(caiGrades);
                caiValues.add(cai);
            }
        }
        res.setBody(body);
        res.setItems(caiValues);
        return res;
    }

    /**
     * 해당 도의 오염물질 별 통합대기환경지수값 조회 API - 전체
     */
    public FineDustCaiResponse getCaiValueListAll(String sidoName){
        return getCaiValueListWithPaging(sidoName,Long.valueOf(-1),Long.valueOf(-1));
    }

    /**
     * 특정 오염물질의 통합대기환경지수값 조회 API
     */
    public FineDustCaiGrade getSltPollutantCaiValue(String pollutant, String stationName){

        // 비어있는 필수 파라미터 값 처리
        if(stationName.equals("")){
            if(pollutant.equals("")){
                FineDustCaiGrade cg =  new FineDustCaiGrade();
                cg.setName("stationName and pollutant is "+MANDATORY_VALUE_ERROR);
                return cg;
            }else{
                FineDustCaiGrade cg =  new FineDustCaiGrade();
                cg.setName("stationName is "+MANDATORY_VALUE_ERROR);
                return cg;
            }
        }else if(stationName.equals("")){
            FineDustCaiGrade cg =  new FineDustCaiGrade();
            cg.setName("pollutant is "+MANDATORY_VALUE_ERROR);
            return cg;
        }

        pollutant = pollutant.toLowerCase();
        // 오염물질이 유효하지 않은 경우
        if(!pollutant.equals("so2") && !pollutant.equals("no2") && !pollutant.equals("co") && !pollutant.equals("o2")
                && !pollutant.equals("o3") && !pollutant.equals("pm10") && !pollutant.equals("pm25")){
            FineDustCaiGrade cg =  new FineDustCaiGrade();
            cg.setName("pollutant is wrong value");
            return cg;
        }

        String contents = requestService.getHttpMeasureStation(stationName, "","");
        // OpenApi 응답 에러 처리
        if(contents.equals(API_ERROR) || contents.equals(TIMEOUT_ERROR)){
            FineDustCaiGrade cg =  new FineDustCaiGrade();
            cg.setName(contents);
            return cg;
        }

        List<FineDustApiItems> apiItems = getItemsResponse(contents);
        //결과값 없을 경우
        if(apiItems == null){
            FineDustCaiGrade cg =  new FineDustCaiGrade();
            cg.setName(NO_RESULT_ERROR);
            return cg;
        }

        FineDustCaiGrade grade = new FineDustCaiGrade();
        grade.setName(pollutant);

        if(apiItems.size() > 0){

            String value = null;
            FineDustApiItems item = apiItems.get(0);

            if(pollutant.equals("so2")){
                value = item.getSo2Value();
                grade.setGrade(item.getSo2Grade());
                if(!value.equals("-")){ grade.setValue( String.valueOf(getSo2Cai(Double.valueOf(value))) ); }
            }else if(pollutant.equals("co")){
                value = apiItems.get(0).getCoValue();
                grade.setGrade(item.getCoGrade());
                if(!value.equals("-")){ grade.setValue( String.valueOf(getCoCai(Double.valueOf(value))) ); }
            }else if(pollutant.equals("no2")){
                value = apiItems.get(0).getNo2Value();
                grade.setGrade(item.getNo2Grade());
                if(!value.equals("-")){ grade.setValue( String.valueOf(getNo2Cai(Double.valueOf(value))) ); }
            }else if(pollutant.equals("o3")){
                value = apiItems.get(0).getO3Value();
                grade.setGrade(item.getO3Grade());
                if(!value.equals("-")){ grade.setValue( String.valueOf(getO3Cai(Double.valueOf(value))) ); }
            }else if(pollutant.equals("pm10")){
                value = apiItems.get(0).getPm10Value24();
                grade.setGrade(item.getPm10Grade());
                if(!value.equals("-")){ grade.setValue( String.valueOf(getPm10Cai(Double.valueOf(value))) ); }
            }else if(pollutant.equals("pm25")){
                value = apiItems.get(0).getPm25Value24();
                grade.setGrade(item.getPm25Grade());
                if(!value.equals("-")){ grade.setValue( String.valueOf(getPm25Cai(Double.valueOf(value))) ); }
            }

            if(value.equals("-")){
                grade.setValue(value);
            }

        }
        return grade;
    }


    /**
     * 각 오염물질 별 통합대기환경지수 계산
     * @param item
     * @return
     */
    public FineDustCaiValue calculateCaiValue(FineDustApiItems item){

        FineDustCaiValue cai = new FineDustCaiValue();
        cai.setDate(item.getDate());
        cai.setStationName(item.getStationName());
        cai.setKhaiValue(item.getKhaiValue());

        //so2
        if(item.getSo2Value().equals("-")){ cai.setCvSo2(item.getSo2Value()); }
        else{ cai.setCvSo2(String.valueOf(getSo2Cai(Double.valueOf(item.getSo2Value())))); }

        //co
        if(item.getCoValue().equals("-")){ cai.setCvCo(item.getCoValue()); }
        else{ cai.setCvCo( String.valueOf(getCoCai(Double.valueOf(item.getCoValue())))); }

        //no2
        if(item.getNo2Value().equals("-")){ cai.setCvNo2(item.getNo2Value()); }
        else{ cai.setCvNo2(String.valueOf(getNo2Cai(Double.valueOf(item.getNo2Value())))); }

        //o3
        if(item.getO3Value().equals("-")){ cai.setCvO3(item.getO3Value()); }
        else{ cai.setCvO3(String.valueOf(getO3Cai(Double.valueOf(item.getO3Value())))); }

        //pm10
        if(item.getPm10Value24().equals("-")){ cai.setCvPm10(item.getPm10Value24()); }
        else{ cai.setCvPm10(String.valueOf(getPm10Cai(Double.valueOf(item.getPm10Value24())))); }

        //pm25
        if(item.getPm25Value24().equals("-")){ cai.setCvPm25(item.getPm25Value24()); }
        else{ cai.setCvPm25(String.valueOf(getPm25Cai(Double.valueOf(item.getPm25Value24())))); }

        return cai;
    }

    /**
     * so2농도 측정값을 이용해 cai 지수 계산
     * @param so2
     * @return
     */
    public long getSo2Cai(double so2){
        double Bp_lo = 0, Bp_hi = 0, I_lo = 0, I_hi = 0;
        if(so2 >=0 && so2 <= 0.02){
            Bp_lo = 0;
            Bp_hi = 0.02;
            I_lo = 0;
            I_hi = 50;
        }else if(so2 >=0.021 && so2 <= 0.05){
            Bp_lo = 0.021;
            Bp_hi = 0.05;
            I_lo = 51;
            I_hi = 100;
        }else if(so2 >=0.051 && so2 <= 0.15){
            Bp_lo = 0.051;
            Bp_hi = 0.15;
            I_lo = 101;
            I_hi = 250;
        }else if(so2 >=0.151 && so2 <= 1){
            Bp_lo = 0.151;
            Bp_hi = 1;
            I_lo = 251;
            I_hi = 500;
        }
        return Math.round( (((I_hi-I_lo)/(Bp_hi-Bp_lo))*(so2-Bp_lo))+I_lo );
    }

    /**
     * co농도 측정값을 이용해 cai 지수 계산
     * @param co
     * @return
     */
    public long getCoCai(double co){
        double Bp_lo = 0, Bp_hi = 0, I_lo = 0, I_hi = 0;
        if(co >=0 && co <= 2){
            Bp_lo = 0;
            Bp_hi = 2;
            I_lo = 0;
            I_hi = 50;
        }else if(co >=2.01 && co <= 9){
            Bp_lo = 2.01;
            Bp_hi = 9;
            I_lo = 51;
            I_hi = 100;
        }else if(co >=9.01 && co <= 15){
            Bp_lo = 9.01;
            Bp_hi = 15;
            I_lo = 101;
            I_hi = 250;
        }else if(co >=15.01 && co <= 50){
            Bp_lo = 15.01;
            Bp_hi = 50;
            I_lo = 251;
            I_hi = 500;
        }
        return Math.round( (((I_hi-I_lo)/(Bp_hi-Bp_lo))*(co-Bp_lo))+I_lo );
    }

    /**
     * o3농도 측정값을 이용해 cai 지수 계산
     * @param o3
     * @return
     */
    public long getO3Cai(double o3){
        double Bp_lo = 0, Bp_hi = 0, I_lo = 0, I_hi = 0;
        if(o3 >=0 && o3 <= 0.03){
            Bp_lo = 0;
            Bp_hi = 0.03;
            I_lo = 0;
            I_hi = 50;
        }else if(o3 >=0.031 && o3 <= 0.09){
            Bp_lo = 0.031;
            Bp_hi = 0.09;
            I_lo = 51;
            I_hi = 100;
        }else if(o3 >=0.091 && o3 <= 0.15){
            Bp_lo = 0.091;
            Bp_hi = 0.15;
            I_lo = 101;
            I_hi = 250;
        }else if(o3 >=0.151 && o3 <= 0.6){
            Bp_lo = 0.151;
            Bp_hi = 0.6;
            I_lo = 251;
            I_hi = 500;
        }
        return Math.round( (((I_hi-I_lo)/(Bp_hi-Bp_lo))*(o3-Bp_lo))+I_lo );
    }

    /**
     * no2농도 측정값을 이용해 cai 지수 계산
     * @param no2
     * @return
     */
    public long getNo2Cai(double no2) {
        double Bp_lo = 0, Bp_hi = 0, I_lo = 0, I_hi = 0;
        if(no2 >=0 && no2 <= 0.03){
            Bp_lo = 0;
            Bp_hi = 0.03;
            I_lo = 0;
            I_hi = 50;
        }else if(no2 >=0.031 && no2 <= 0.06){
            Bp_lo = 0.031;
            Bp_hi = 0.06;
            I_lo = 51;
            I_hi = 100;
        }else if(no2 >=0.061 && no2 <= 0.2){
            Bp_lo = 0.061;
            Bp_hi = 0.2;
            I_lo = 101;
            I_hi = 250;
        }else if(no2 >=0.201 && no2 <= 2){
            Bp_lo = 0.201;
            Bp_hi = 2;
            I_lo = 251;
            I_hi = 500;
        }
        return Math.round( (((I_hi-I_lo)/(Bp_hi-Bp_lo))*(no2-Bp_lo))+I_lo );
    }

    /**
     * pm10 24시간 예측 평균값을 이용해 cai 지수 계산
     * @param pm10
     * @return
     */
    public long getPm10Cai(double pm10) {
        double Bp_lo = 0, Bp_hi = 0, I_lo = 0, I_hi = 0;
        if(pm10 >=0 && pm10 <= 30){
            Bp_lo = 0;
            Bp_hi = 30;
            I_lo = 0;
            I_hi = 50;
        }else if(pm10 >=31 && pm10 <= 80){
            Bp_lo = 31;
            Bp_hi = 80;
            I_lo = 51;
            I_hi = 100;
        }else if(pm10 >=81 && pm10 <= 150){
            Bp_lo = 81;
            Bp_hi = 150;
            I_lo = 101;
            I_hi = 250;
        }else if(pm10 >=151 && pm10 <= 600){
            Bp_lo = 151;
            Bp_hi = 600;
            I_lo = 251;
            I_hi = 500;
        }
        return Math.round( (((I_hi-I_lo)/(Bp_hi-Bp_lo))*(pm10-Bp_lo))+I_lo );
    }

    /**
     * pm25 24시간 예측 평균값을 이용해 cai 지수 계산
     * @param pm25
     * @return
     */
    public long getPm25Cai(double pm25){
        double Bp_lo = 0, Bp_hi = 0, I_lo = 0, I_hi = 0;
        if(pm25 >=0 && pm25 <= 15){
            Bp_lo = 0;
            Bp_hi = 15;
            I_lo = 0;
            I_hi = 50;
        }else if(pm25 >=16 && pm25 <= 35){
            Bp_lo = 16;
            Bp_hi = 35;
            I_lo = 51;
            I_hi = 100;
        }else if(pm25 >=36 && pm25 <= 75){
            Bp_lo = 36;
            Bp_hi = 75;
            I_lo = 101;
            I_hi = 250;
        }else if(pm25 >=76 && pm25 <= 500){
            Bp_lo = 76;
            Bp_hi = 500;
            I_lo = 251;
            I_hi = 500;
        }
        return Math.round( (((I_hi-I_lo)/(Bp_hi-Bp_lo))*(pm25-Bp_lo))+I_lo );
    }

}
