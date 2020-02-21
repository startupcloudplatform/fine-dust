'use strict';

//angular.
//module('fineDustApp').
app.service("fineDustService", function($http) {
    //초기 데이터 가져오기
    this.getDustAverageSido = function(pollutant) {
        return $http({
            method: 'GET',
            url: '/api/dust/measure/avg/sido',
            params: {'pollutant': pollutant},
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).then(function success(response){
            return response.data.items[0];
        }).catch(function error(response){
            console.log("fail -- /api/dust/measure/avg/sido --");
            console.log(response);
        });
    };


    // 우측 상단 데이터
    this.makeStandardID = function(defaultId) {
        var strArray = defaultId.split('-');
        if(strArray.length === 1) {
            return defaultId.toLowerCase();
        } else if (strArray.length === 2) {
            return strArray[1].toLowerCase();
        } else {
            return '-';
        }
    };


    this.getDustAverageSigunguTime = function(sido, time) {
        var hour = time.split(' ');
        var params ={
            sidoName: EnglishIdChangeToKoreanSido(sido),
            hour: hour[1]
        }
        return $http({
            method: 'GET',
            url: '/api/dust/measure/avg/time/sigungu',
            params: params,
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).then(function success(response){
            return response.data.items;
        }).catch(function error(response){
            console.log("fail -- /api/dust/measure/avg/time/sigungu--");
            console.log(response);
        });
    };

    this.getDustCaiListALL = function(sido) {
        var params ={
            sidoName: EnglishIdChangeToKoreanSido(sido)
        };

        console.log("Func -- getDustCaiListAll --");
        console.log(params);
        return $http({
            method: 'GET',
            url: '/api/dust/cai/list/all',
            params: params,
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).then(function success(response){
            return response.data.items;
        }).catch(function error(response){
            console.log("fail -- /api/dust/cai/list/all --");
            console.log(response);
        });
    };

    var EnglishIdChangeToKoreanSido = function(englishSido) {
        if (englishSido === "busan") {
            return "부산";
        } else if (englishSido === "daegu") {
            return "대구";
        } else if (englishSido==="incheon") {
            return "인천";
        } else if (englishSido==="gwangju") {
            return "광주";
        } else if (englishSido==="daejeon") {
            return "대전";
        } else if (englishSido==="ulsan") {
            return "울산";
        } else if (englishSido==="sejong") {
            return "세종";
        } else if (englishSido==="gyeonggi") {
            return "경기";
        } else if (englishSido==="gangwon") {
            return "강원";
        } else if (englishSido==="chungbuk") {
            return "충북";
        } else if (englishSido==="chungnam") {
            return "충남";
        } else if (englishSido==="jeonbuk") {
            return "전북";
        } else if (englishSido==="jeonnam") {
            return "전남";
        } else if (englishSido==="gyeongbuk") {
            return "경북";
        } else if (englishSido==="gyeongnam") {
            return "경남";
        } else if (englishSido==="jeju") {
            return "제주";
        } else {
            return "서울";
        }
    }
});