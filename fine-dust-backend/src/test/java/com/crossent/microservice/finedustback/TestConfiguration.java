package com.crossent.microservice.finedustback;

import com.crossent.microservice.finedustback.api.dto.*;
import java.util.ArrayList;
import java.util.List;

public class TestConfiguration {
    public static final String SIDO_NAME = "서울";
    public static final String STATION_NAME = "강남구";
    public static final Long PAGE_NO = Long.valueOf(1);
    public static final Long NUM_OF_ROWS = Long.valueOf(20);
    public static final String HOUR = "15:00";
    public static final String POLLUTANT = "pm25";
    public static final String CONTENTS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<response>\n\t<header>\n\t\t<resultCode>00</resultCode>\n\t\t<resultMsg>NORMAL SERVICE.</resultMsg>\n" +
            "\t</header>\n\t<body>\n\t\t<items>\n\t\t\t\t<item>\n\t\t\t\t\t<stationName>중구</stationName>\n" +
            "\t\t\t\t\t<dataTime>2019-12-16 13:00</dataTime>\n\t\t\t\t\t<so2Value>0.004</so2Value>\n" +
            "\t\t\t\t\t<coValue>0.8</coValue>\n\t\t\t\t\t<o3Value>0.007</o3Value>\n" +
            "\t\t\t\t\t<no2Value>0.041</no2Value>\n\t\t\t\t\t<pm10Value>33</pm10Value>\n" +
            "\t\t\t\t\t<pm10Value24>31</pm10Value24>\n\t\t\t\t\t<pm25Value>23</pm25Value>\n" +
            "\t\t\t\t\t<pm25Value24>23</pm25Value24>\n\t\t\t\t\t<khaiValue>69</khaiValue>\n" +
            "\t\t\t\t\t<khaiGrade>2</khaiGrade>\n\t\t\t\t\t<so2Grade>1</so2Grade>\n" +
            "\t\t\t\t\t<coGrade>1</coGrade>\n\t\t\t\t\t<o3Grade>1</o3Grade>\n" +
            "\t\t\t\t\t<no2Grade>2</no2Grade>\n\t\t\t\t\t<pm10Grade>2</pm10Grade>\n" +
            "\t\t\t\t\t<pm25Grade>2</pm25Grade>\n\t\t\t\t</item>\n\t\t\t\t<item>\n" +
            "\t\t\t\t\t<stationName>한강대로</stationName>\n\t\t\t\t\t<dataTime>2019-12-16 13:00</dataTime>\n" +
            "\t\t\t\t\t<so2Value>0.005</so2Value>\n\t\t\t\t\t<coValue>0.9</coValue>\n" +
            "\t\t\t\t\t<o3Value>0.006</o3Value>\n\t\t\t\t\t<no2Value>0.044</no2Value>\n" +
            "\t\t\t\t\t<pm10Value>46</pm10Value>\n\t\t\t\t\t<pm10Value24>38</pm10Value24>\n" +
            "\t\t\t\t\t<pm25Value>26</pm25Value>\n\t\t\t\t\t<pm25Value24>23</pm25Value24>\n" +
            "\t\t\t\t\t<khaiValue>73</khaiValue>\n\t\t\t\t\t<khaiGrade>2</khaiGrade>\n" +
            "\t\t\t\t\t<so2Grade>1</so2Grade>\n\t\t\t\t\t<coGrade>1</coGrade>\n" +
            "\t\t\t\t\t<o3Grade>1</o3Grade>\n\t\t\t\t\t<no2Grade>2</no2Grade>\n" +
            "\t\t\t\t\t<pm10Grade>2</pm10Grade>\n\t\t\t\t\t<pm25Grade>2</pm25Grade>\n" +
            "\t\t\t\t</item>\n\t\t</items>\n\t\t\t<numOfRows>10</numOfRows>\n\t\t\t<pageNo>1</pageNo>\n" +
            "\t\t\t<totalCount>40</totalCount>\n\t</body>\n" +
            "</response>\n";

    // 시도별 실시간 측정값 리턴
    public static FineDustValueResponse getFineDustValueResponse(){
        FineDustValueResponse res = new FineDustValueResponse();

        FineDustBody body = new FineDustBody(2, 60,30);

        List<FineDustValueItems> items = new ArrayList<>();
        items.add(new FineDustValueItems("2019-12-13 14:00", "중구","0.004","0.7",
                "0.042","0.005","31","22","29","21"));

        items.add(new FineDustValueItems("2019-12-13 14:00", "한강대로","0.005","0.7",
                "0.039","0.005","36","19","36","19"));

        res.setItems(items);
        res.setBody(body);

        return res;
    }

    // 시도별 실시간 측정값을 이용한 cai 값 리턴
    public static FineDustCaiResponse getFineDustCaiResponse(){

        FineDustCaiResponse res = new FineDustCaiResponse();
        FineDustBody body = new FineDustBody(1,50,30);

        List<FineDustCaiValue> list = new ArrayList<>();

        List<FineDustCaiGrade> grades = new ArrayList<>();
        grades.add(new FineDustCaiGrade("so2","10","1"));
        grades.add(new FineDustCaiGrade("co","15","1"));
        grades.add(new FineDustCaiGrade("no2","68","2"));
        grades.add(new FineDustCaiGrade("o3","10","1"));
        grades.add(new FineDustCaiGrade("pm10","55","2"));
        grades.add(new FineDustCaiGrade("pm25","26","2"));
        grades.add(new FineDustCaiGrade("khaiValue","68","2"));

        FineDustCaiValue value = new FineDustCaiValue("동대문구","2019-12-13 14:00", "10","15","10",
                                                        "68","55","56","68", grades);

        list.add(value);
        res.setItems(list);
        res.setBody(body);

        return res;
    }

    // 해당 오염물질 cai 값, 등급 리턴
    public static FineDustCaiGrade getFineDustCaiGrade(){
        return new FineDustCaiGrade("pm25","72","2");
    }

    // 실시간 시군구별 평균데이터 리턴
    public static FineDustApiResponse getFineDustApiResponse(){

        FineDustApiResponse res = new FineDustApiResponse();
        FineDustBody body = new FineDustBody(1,20,10);

        List<FineDustApiItems> items = new ArrayList<>();
        FineDustApiItems item1 = new FineDustApiItems();
        item1.setDate("2019-12-13 16:00");
        item1.setCityName("강동구");
        item1.setSo2Value("0.003");
        item1.setCoValue("0.4");
        item1.setNo2Value("0.04");
        item1.setO3Value("0.007");
        item1.setPm10Value("35");
        item1.setPm10Value("22");
        items.add(item1);

        FineDustApiItems item2 = new FineDustApiItems();
        item2.setDate("2019-12-13 16:00");
        item2.setCityName("강북구");
        item2.setSo2Value("0.004");
        item2.setCoValue("0.5");
        item2.setNo2Value("0.053");
        item2.setO3Value("0.01");
        item2.setPm10Value("28");
        item2.setPm10Value("15");
        items.add(item2);

        res.setItems(items);
        res.setBody(body);

        return res;
    }

    // 시도별 평균 데이터 리턴
    public static FineDustApiResponse getMeasureAvgSidoData(){
        FineDustApiResponse res = new FineDustApiResponse();

        FineDustBody body = new FineDustBody(1,50,10);

        List<FineDustApiItems> items = new ArrayList<>();
        FineDustApiItems item = new FineDustApiItems();
        item.setDate("2019-12-13 15:00");
        item.setPollutant("NO2");
        item.setSearchCondition("시간평균");
        item.setSeoul("0.037");
        item.setBusan("0.027");
        item.setDaegu("0.027");
        item.setIncheon("0.03");
        item.setGwangju("0.025");
        item.setDaejeon("0.035");
        item.setUlsan("0.024");
        item.setGyeonggi("0.029");
        item.setGangwon("0.015");
        item.setChungbuk("0.021");
        item.setChungnam("0.013");
        item.setJeonbuk("0.012");
        item.setJeonnam("0.016");
        item.setGyeongbuk("0.018");
        item.setGyeongnam("0.024");
        item.setJeju("0.01");
        item.setSejong("0.032");
        items.add(item);

        res.setItems(items);
        res.setBody(body);

        return res;
    }


}
