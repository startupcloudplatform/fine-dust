package com.crossent.microservice.finedustback.api.service;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
    private static final String API_ERROR = "OpenApi Response Error";
    private static final String TIMEOUT_ERROR = "Timeout Error";
    private static final String PAGE_SIZE = "1000";
    private static final String PAGE_NUM = "1";
    private static final String VERSION = "1.1";
    private static final String DATA_TERM_DAILY = "DAILY";
    private static final String DATA_TERM_HOUR = "HOUR";
    private static final String DATA_TERM_MONTH = "MONTH";
    private static final String POLLUTANT = "PM25";

    // 시도별 실시간 측정정보
    @Value("${openData.market.measure.sido.api:}")
    private String measureSidoApi;

    // 측정소별 실시간 측정정보
    @Value("${openData.market.measure.station.api}")
    private String measureStationApi;

    // 시군구별 실시간 평균정보
    @Value("${openData.market.measure.avg.sigungu.api}")
    private String measureAvgSigunguApi;

    // 시도별 실시간 평균정보
    @Value("${openData.market.measure.avg.sido.api}")
    private String measureAvgSidoApi;

    // 인증키
    @Value("${openData.market.authKey:}")
    private String authKey;

    OkHttpClient client = null;

    public RequestService(){};
    public RequestService(String authKey, OkHttpClient client){
        this.authKey = authKey;
        this.client = client;
    }


    /**
     * 시도별 실시간 측정정보 api
     * 페이징과 전체 출력 공통으로 사용하는 메소드
     * pageNo == -1 이고 numOfRows == -1 이면 전체 페이지 출력
     * @param sidoName
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public String getHttpMeasureSido(String sidoName, String pageNo, String numOfRows){

        String contents = "";
        if( client == null ){
            client = new OkHttpClient();
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }

        if(pageNo == null || pageNo.equals("") || pageNo.equals("-1") || pageNo.equals("null")){
            pageNo = PAGE_NUM;
        }

        if(numOfRows == null || numOfRows.equals("") || numOfRows.equals("-1") || numOfRows.equals("null")){
            numOfRows = PAGE_SIZE;
        }

        try{
            HttpUrl httpUrl = HttpUrl.parse(measureSidoApi)
                    .newBuilder()
                    .addQueryParameter("auth_key", authKey)
                    .addQueryParameter("numOfRows", numOfRows)
                    .addQueryParameter("pageNo", pageNo)
                    .addQueryParameter("sidoName", sidoName)
                    .addQueryParameter("ver",VERSION).build();

            String url = httpUrl.toString();
            Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/json")
                    .addHeader("ACCEPT", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if( response.code() == HttpStatus.OK.value() && response.body() != null){
                contents  = response.body().string();
            }else{
                contents = API_ERROR;
            }
        }catch (Exception e){
            System.out.println("====Error in getHttpMeasureSido(String sidoName, String pageNo, String numOfRows)====");
            if(e.getClass().equals(NullPointerException.class)){
                System.out.println("==== NullPointerException ====");
                contents = API_ERROR;

            }else if(e.getClass().equals(TimeoutException.class)){
                System.out.println("==== TimeoutException ====");
                contents = TIMEOUT_ERROR;
            }
            logger.debug(e.toString());
        }

        logger.info("contents : "+contents);
        return contents;
    }

    /**
     * 측정소별 실시간 측정정보 api
     * 페이징과 전체 출력 공통으로 사용하는 메소드
     * pageNo == -1 이고 numOfRows == -1 이면 전체 페이지 출력
     * @param stationName
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public String getHttpMeasureStation(String stationName, String pageNo, String numOfRows){

        String contents = "";
        if( client == null ){
            client = new OkHttpClient();
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }

        if(pageNo == null || pageNo.equals("") || pageNo.equals("-1") || pageNo.equals("null")){
            pageNo = PAGE_NUM;
        }

        if(numOfRows == null || numOfRows.equals("") || numOfRows.equals("-1") || numOfRows.equals("null")){
            numOfRows = PAGE_SIZE;
        }

        try{
            HttpUrl httpUrl = HttpUrl.parse(measureStationApi)
                    .newBuilder()
                    .addQueryParameter("auth_key", authKey)
                    .addQueryParameter("numOfRows", numOfRows)
                    .addQueryParameter("pageNo", pageNo)
                    .addQueryParameter("stationName", stationName)
                    .addQueryParameter("dataTerm", DATA_TERM_DAILY)
                    .addQueryParameter("ver",VERSION).build();

            String url = httpUrl.toString();
            Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/json")
                    .addHeader("ACCEPT", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if( response.code() == HttpStatus.OK.value() && response.body() != null){
                contents  = response.body().string();
            }else{
                contents = API_ERROR;
            }
        }catch (Exception e){
            System.out.println("====Error in getHttpMeasureStation(String stationName, String pageNo, String numOfRows)====");
            if(e.getClass().equals(NullPointerException.class)){
                System.out.println("==== NullPointerException ====");
                contents = API_ERROR;

            }else if(e.getClass().equals(TimeoutException.class)){
                System.out.println("==== TimeoutException ====");
                contents = TIMEOUT_ERROR;
            }
            logger.debug(e.toString());
        }

        logger.info("contents : "+contents);
        return contents;

    }


    /**
     * 시군구별 실시간 평균정보 api
     * 페이징과 전체 출력 공통으로 사용하는 메소드
     * pageNo == -1 이고 numOfRows == -1 이면 전체 페이지 출력
     * @param sidoName
     * @return
     */
    public String getHttpMeasureAvgSigungu(String sidoName){

        String contents = "";
        if( client == null ){
            client = new OkHttpClient();
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }

        try{
            HttpUrl httpUrl = HttpUrl.parse(measureAvgSigunguApi)
                    .newBuilder()
                    .addQueryParameter("auth_key", authKey)
                    .addQueryParameter("numOfRows", PAGE_SIZE)
                    .addQueryParameter("pageNo", PAGE_NUM)
                    .addQueryParameter("sidoName", sidoName)
                    .addQueryParameter("searchCondition",DATA_TERM_DAILY).build();

            String url = httpUrl.toString();
            Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/json")
                    .addHeader("ACCEPT", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if( response.code() == HttpStatus.OK.value() && response.body() != null){
                contents  = response.body().string();
            }else{
                contents = API_ERROR;
            }
        }catch (Exception e){
            System.out.println("====Error in getHttpMeasureAvgSigungu(String sidoName)====");
            if(e.getClass().equals(NullPointerException.class)){
                System.out.println("==== NullPointerException ====");
                contents = API_ERROR;

            }else if(e.getClass().equals(TimeoutException.class)){
                System.out.println("==== TimeoutException ====");
                contents = TIMEOUT_ERROR;
            }
            logger.debug(e.toString());
        }

        logger.info("contents : "+contents);
        return contents;

    }

    /**
     * 시도별 실시간 평균정보 api
     * 오염물질 default = pm25
     * pageNo == -1 이고 numOfRows == -1 이면 전체 페이지 출력
     * @param pollutant
     * @return
     */
    public String getHttpMeasureAvgSidoApi(String pollutant){

        String contents = "";
        if( client == null ){
            client = new OkHttpClient();
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }

        if(pollutant == null || pollutant.equals("") || pollutant.equals("null")){
            pollutant = POLLUTANT;
        }

        try{
            HttpUrl httpUrl = HttpUrl.parse(measureAvgSidoApi)
                    .newBuilder()
                    .addQueryParameter("auth_key", authKey)
                    .addQueryParameter("numOfRows", PAGE_SIZE)
                    .addQueryParameter("pageNo", PAGE_NUM)
                    .addQueryParameter("itemCode", pollutant)
                    .addQueryParameter("dataGubun", DATA_TERM_HOUR)
                    .addQueryParameter("searchCondition",DATA_TERM_MONTH).build();

            String url = httpUrl.toString();
            Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/json")
                    .addHeader("ACCEPT", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if( response.code() == HttpStatus.OK.value() && response.body() != null){
                contents  = response.body().string();
            }else{
                contents = API_ERROR;
            }
        }catch (Exception e){
            System.out.println("====Error in getHttpMeasureAvgSidoApi(String pollutant)====");
            if(e.getClass().equals(NullPointerException.class)){
                System.out.println("==== NullPointerException ====");
                contents = API_ERROR;

            }else if(e.getClass().equals(TimeoutException.class)){
                System.out.println("==== TimeoutException ====");
                contents = TIMEOUT_ERROR;
            }
            logger.debug(e.toString());
        }

        logger.info("contents : "+contents);
        return contents;

    }

}