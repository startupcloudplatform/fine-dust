package com.crossent.msa.example.configuration;

import com.crossent.msa.example.dto.DustAverageSido;

import java.util.*;

public class DustMeasureConfiguration {
    public static Map<String, Integer> setDustAverageSido() {
        Map<String, Integer> map = new HashMap<>();
        map.put( "seoul",34);
        map.put("busan", 26);
        map.put("daegu", 16);
        map.put("incheon", 36);
        map.put("gwangju", 26);
        map.put("daejeon", 34);
        map.put("ulsan", 19);
        map.put("gyeonggi", 38);
        map.put("gangwon", 24);
        map.put("chungbuk", 33);
        map.put("chungnam", 32);
        map.put("jeonbuk", 25);
        map.put("jeonnam", 18);
        map.put("gyeongbuk", 27);
        map.put("gyeongnam", 30);
        map.put("jeju", 18);
        map.put("sejong", 18);

        return map;
    }
    /*
    public static List<DustAverageSido> setDustAverageSido() {
        List<DustAverageSido> dustAverageSidoList = new ArrayList<>(Arrays.asList(
                new DustAverageSido( "seoul",34),
                new DustAverageSido("busan", 26),
                new DustAverageSido("daegu", 16),
                new DustAverageSido("incheon", 36),
                new DustAverageSido("gwangju", 26),
                new DustAverageSido("daejeon", 34),
                new DustAverageSido("ulsan", 19),

                new DustAverageSido("gyeonggi", 38),
                new DustAverageSido("gangwon", 24),

                new DustAverageSido("chungbuk", 33),
                new DustAverageSido("chungnam", 32),

                new DustAverageSido("jeonbuk", 25),
                new DustAverageSido("jeonnam", 18),

                new DustAverageSido("gyeongbuk", 27),
                new DustAverageSido("gyeongnam", 30),

                new DustAverageSido("jeju", 18),
                new DustAverageSido("sejong", 18)
        ));
        return dustAverageSidoList;
    }
    */

}
