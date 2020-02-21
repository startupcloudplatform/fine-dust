'use strict';
app.controller('appMain', function AppMainController($scope, fineDustService) {
    /**
     *
     * @type {AppMainController}
     *
     * html에서 사용할 변수 선언부
     */
    var mainSelf = this;
    mainSelf.dustData ={
        pm25: '',
        pm10: '',
        so2: '',
        co: '',
        no2: '',
        o3: ''
    };
    mainSelf.selectedSido = '';
    mainSelf.dataTime = '';
    mainSelf.sigunguDustDataList = '';
    mainSelf.caiInputStation = '';

    mainSelf.colorsEven = ['#5a9bd5', '#ec7c31', '#a5a5a5'];
    mainSelf.cityNameList = [];
    mainSelf.coValueList = [];
    mainSelf.no2ValueList = [];
    mainSelf.o3ValueList = [];
    mainSelf.so2ValueList = [];

    mainSelf.dustCaiDataList = null;
    mainSelf.dustCaiGrades = null;

    /**
     * chart Line
     */
    mainSelf.labelsChart1 = null;
    mainSelf.seriesChart1 = null;
    mainSelf.dataChart1 = null;
    mainSelf.datasetOverrideChart1 = null; //, { yAxisID: 'y-axis-2' }];
    mainSelf.optionsChart1 = null;


    /**
     * chart Radar
     * @type {null}
     */
    mainSelf.labelsChart2 = null;
    mainSelf.dataChart2 = null;

    /**
     * 변수 초기화
     */
    mainSelf.setInitData = function() {
        console.log("Func -- Set Init Data --");
        /* loading state
        $scope.$parent.$parent == appCommon
        */
        $scope.$parent.$parent.loadingState = true;

        // null로 저장하면 rendering 시에 error 발생
        fineDustService.getDustAverageSido("pm25").then(function (response) {
            mainSelf.dustData.pm25 = response;
            mainSelf.dataTime = response['dataTime'];
            if(!mainSelf.dataTime) {
                alert("초기 데이터를 받아오지 못했습니다. 새로고침 해주세요.")
            }else {
                if(!mainSelf.selectedSido) {
                    mainSelf.selectedSido = "seoul";
                }
                fineDustService.getDustAverageSigunguTime(mainSelf.selectedSido, mainSelf.dataTime).then(function (response) {
                    console.log("Func --sigungu--");
                    mainSelf.sigunguDustDataList = response;
                    for (var i in response) {
                        mainSelf.cityNameList.push(response[i].cityName);
                        mainSelf.coValueList.push(response[i].coValue);
                        mainSelf.no2ValueList.push(response[i].no2Value);
                        mainSelf.o3ValueList.push(response[i].o3Value);
                        mainSelf.so2ValueList.push(response[i].so2Value);
                    };

                    // Draw LineChart
                    drawLineChart(mainSelf.cityNameList,
                        ['일산화탄소', '아황산가스', '이산화질소'],
                        [
                            mainSelf.coValueList,
                            mainSelf.so2ValueList,
                            mainSelf.no2ValueList
                        ]
                    );
                });

                fineDustService.getDustCaiListALL(mainSelf.selectedSido).then(function (response) {
                    mainSelf.dustCaiDataList = response;
                    mainSelf.dustCaiGrades = findCaiDataUsingStationName(null); //--()

                    //Draw RadarChart
                    drawRadarChart(["아황산가스", "일산화탄소", "이산화질소", "오존", "미세먼지", "초미세먼지"],
                        [
                            //[8, 10, 15, 50, 22, 40]
                            caiDataToValueList(mainSelf.dustCaiGrades)
                        ]
                    );
                });
            };
        });
        fineDustService.getDustAverageSido("pm10").then(function (value) {
            mainSelf.dustData.pm10 = value;
        });
        fineDustService.getDustAverageSido("so2").then(function (value) {
            mainSelf.dustData.so2 = value;
        });
        fineDustService.getDustAverageSido("co").then(function (value) {
            mainSelf.dustData.co = value;
        });
        fineDustService.getDustAverageSido("no2").then(function (value) {
            mainSelf.dustData.no2 = value;
        });
        fineDustService.getDustAverageSido("o3").then(function (value) {
            mainSelf.dustData.o3 = value;
            $scope.$parent.$parent.loadingState = false;
        });

    };
    mainSelf.setInitData();

    /**
     *
     *
     * mainSelf.setSvgSidoGrade()
     *
     * mainSelf.selectSido()
     */
    mainSelf.setPm25Grade = function(pm25Value) {
        if (isNaN(pm25Value)) {
          return pm25Value;
        } else if (pm25Value < 16) {
            return "GOOD";
        }else if (pm25Value < 36) {
            return "NORMAL";
        }else if (pm25Value < 76) {
            return "BAD";
        } else {
            return "VERYBAD";
        }
    };

    mainSelf.setPm10Grade = function(pm10Value) {
        if (isNaN(pm10Value)) {
            return pm10Value;
        } else if (pm10Value < 31) {
            return "GOOD";
        }else if (pm10Value < 81) {
            return "NORMAL";
        }else if (pm10Value < 151) {
            return "BAD";
        }else {
            return "VERYBAD";
        }
    };

    mainSelf.changeEnglishGradeToKorean = function(englishGrade) {
      if (englishGrade === "GOOD") {
          return "좋음";
      } else if (englishGrade === "NORMAL") {
          return "보통";
      } else if (englishGrade === "BAD") {
          return "나쁨";
      } else if (englishGrade === "VERYBAD") {
          return "매우 나쁨";
      } else {
          return englishGrade;
      }
    };

    mainSelf.changeEnglishPolluteToKorean = function(englishPollute) {
        if (englishPollute === "so2") {
            return "아황산가스";
        } else if (englishPollute === "co") {
            return "일산화탄소";
        } else if (englishPollute === "no2") {
            return "이산화질소";
        } else if (englishPollute === "o3") {
            return "오존";
        } else if (englishPollute === "pm10") {
            return "미세 먼지";
        } else if (englishPollute === "pm25") {
            return "초미세 먼지";
        }else {
            return englishPollute;
        }
    };

    mainSelf.changeTextByselected = function(sidoId) {
        if (sidoId === mainSelf.selectedSido) {
            return "selectedTEXT";
        } else {
            if (!mainSelf.selectedSido) {
                if (sidoId === "seoul") {
                    return "selectedTEXT";
                } else {
                    return "nonSelectedTEXT";
                }
            } else {
                return "nonSelectedTEXT";
            }
        }
    };

    mainSelf.setCaiGrade = function(value) {
        if (value === "1") {
            return ["circle-level", "good"];
        } else if (value === "2") {
            return ["circle-level", "normal"];
        } else if (value === "3") {
            return ["circle-level", "bad"];
        } else if (value === "4") {
            return ["circle-level", "very-bad"];
        } else {
            return "";
        }
    };

    var drawLineChart = function(xAxisList, dataTypeList ,dataChartList) {
        /**
         * chart1 init
         */
        mainSelf.labelsChart1 = xAxisList;
        mainSelf.seriesChart1 = dataTypeList;
        mainSelf.dataChart1 = dataChartList;

        mainSelf.datasetOverrideChart1 = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }, { yAxisID: 'y-axis-3' }];
        mainSelf.optionsChart1 = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    },
                    {
                        id: 'y-axis-2',
                        type: 'linear',
                        display: true,
                        position: 'right'
                    },
                    {
                        id: 'y-axis-3',
                        type: 'linear',
                        display: true,
                        position: 'right'
                    }
                ]
            }
        };
    };

    var drawRadarChart = function(dataTypeList ,dataChartList) {
        /**
         * chart2
         * @type {string[]}

        mainSelf.labelsChart2 =["아황산가스", "일산화탄소", "오존", "이산화질소", "미세먼지", "초미세먼지"];
        mainSelf.dataChart2 = [
            [8, 10, 15, 50, 22, 40]
        ];
         */
        mainSelf.labelsChart2 = dataTypeList;
        mainSelf.dataChart2 = dataChartList;
    };


    /**
     * cai value
     * @param station
     * @returns {*}
     */
    var findCaiDataUsingStationName = function(station) {
        console.log("Func -- findCaiDataUsingStationName --");
        console.log("station: " +station);
        console.log("data 목록");
        console.log(mainSelf.dustCaiDataList);
        if(!station) {
            if(mainSelf.dustCaiDataList.length > 0) {
                if (mainSelf.dustCaiDataList[0].stationName !== "No Result") {
                    // grades.slice(0, 6) --> khaiValue를 사용 안하기 위해서
                    return mainSelf.dustCaiDataList[0].grades.slice(0,6);
                } else {
                    return null;
                }
            }
        } else {
            for(var i in mainSelf.dustCaiDataList) {
                if(station === mainSelf.dustCaiDataList[i].stationName) {
                    return mainSelf.dustCaiDataList[i].grades.slice(0,6);
                }
            }
        }
        return null;
    };
    var caiDataToValueList = function(caiDataGrades) {
        console.log("caiDataToValueList");
        console.log(caiDataGrades);
        var caiValueList = [];
        if (!caiDataGrades || caiDataGrades.length < 1) {
            return caiValueList;
        } else{
            for(var i in caiDataGrades) {
                if (caiDataGrades[i].name === "khaiValue") {
                    break;
                } else {
                    caiValueList.push(caiDataGrades[i].value);
                }
            }
            return caiValueList;
        }

    };


    mainSelf.selectSido = function(event) {
        mainSelf.selectedSido = fineDustService.makeStandardID(event.target.id)
        mainSelf.cityNameList = [];
        mainSelf.coValueList = [];
        mainSelf.no2ValueList = [];
        mainSelf.o3ValueList = [];
        mainSelf.so2ValueList = [];
        mainSelf.caiInputStation = '';

        /* loading state
         $scope.$parent.$parent == appCommon
         */
        $scope.$parent.$parent.loadingState = true;

        fineDustService.getDustAverageSigunguTime(mainSelf.selectedSido,mainSelf.dataTime).then(function(response) {
            mainSelf.sigunguDustDataList = response;
            for (var i in response) {
                mainSelf.cityNameList.push(response[i].cityName);
                mainSelf.coValueList.push(response[i].coValue);
                mainSelf.no2ValueList.push(response[i].no2Value);
                mainSelf.o3ValueList.push(response[i].o3Value);
                mainSelf.so2ValueList.push(response[i].so2Value);
            };

            // Draw LineChart
            drawLineChart(mainSelf.cityNameList,
                ['일산화탄소', '아황산가스', '이산화질소'],
                [
                    mainSelf.coValueList,
                    mainSelf.so2ValueList,
                    mainSelf.no2ValueList
                ]
            );
        });

        fineDustService.getDustCaiListALL(mainSelf.selectedSido).then(function(response){
            mainSelf.dustCaiDataList = response;
            mainSelf.dustCaiGrades = findCaiDataUsingStationName(null);//--()
            //Draw RadarChart
            drawRadarChart(["아황산가스", "일산화탄소", "이산화질소", "오존", "미세먼지", "초미세먼지"],
                [
                    //[8, 10, 15, 50, 22, 40]
                    caiDataToValueList(mainSelf.dustCaiGrades)
                ]
            );
            $scope.$parent.$parent.loadingState = false;
        });


    };

    /**
     * 레이더 값 input 처리
     */
    mainSelf.caiClick = function(station) {
        console.log("Func -- Cai Click --");
        console.log(station);
        /* loading state
        $scope.$parent.$parent == appCommon
        */
        $scope.$parent.$parent.loadingState = true;
        if (!station) {
            alert("측정소 이름을 입력해주세요.");
        } else {
            mainSelf.dustCaiGrades = findCaiDataUsingStationName(station);
            if(!mainSelf.dustCaiGrades) {
                alert("없는 측정소 입니다.");
            } else {
                drawRadarChart(["아황산가스", "일산화탄소", "이산화질소", "오존", "미세먼지", "초미세먼지"],
                    [
                        //[8, 10, 15, 50, 22, 40]
                        caiDataToValueList(mainSelf.dustCaiGrades)
                    ]
                );
            }
            $scope.$parent.$parent.loadingState = false;
        }
    };

    //-----------------------------------------------------------------------------------------------------------------

    /**
     * chart1
     * @param points
     * @param evt
     */
    mainSelf.onClick = function (points, evt) {
        console.log(points, evt);
    };

});