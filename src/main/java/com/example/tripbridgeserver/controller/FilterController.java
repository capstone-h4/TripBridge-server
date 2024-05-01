package com.example.tripbridgeserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class FilterController {

    //서비스,dto 등 분리하기
    // 공공 api 이용한 장소 필터링 기능
    @GetMapping("/place/list")
    public String showPlace(
          @RequestParam("areaCode") String areaCode,
          @RequestParam("contentTypeId") String contentTypeId,
          @RequestParam("cat1") String cat1,
          @RequestParam("cat2") String cat2,
          @RequestParam(value = "cat3", required = false, defaultValue = "") String cat3
          ){

        StringBuffer result = new StringBuffer();
        try{ // serviceKey는 encoding serviceKey 사용
            String urlstr = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?" +
                    "numOfRows=100" +
                    "&MobileOS=ETC" +
                    "&MobileApp=TripBridge" +
                    "&areaCode=" + areaCode +
                    "&contentTypeId=" + contentTypeId +
                    "&cat1=" + cat1 +
                    "&cat2=" + cat2 +
                    "&cat3=" + cat3 +
                    "&serviceKey=YGF0y1fe6pkkysTM1WzMH9htWqLp7iiY2fuy%2BJiEFoI%2BKH%2BON1EnErgGxvE6T2Z5awLIIAlQcuD9hjylPI6GYg%3D%3D" +
                    "&_type=json";
            URL url = new URL(urlstr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String returnLine;
            //result.append("<pre>");
            while((returnLine = br.readLine()) != null){
                result.append(returnLine+ "\n");
            }
            urlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();

        }

        //return result+"</pre>";
        return result.toString();
    }





}
