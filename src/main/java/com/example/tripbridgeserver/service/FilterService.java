package com.example.tripbridgeserver.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Service
public class FilterService {
    public String getFilteredPlaces(String areaCode, String contentTypeId, String cat1, String cat2, String cat3) throws FilterServiceException {
        try {
            String urlstr = variableUrl(areaCode, contentTypeId, cat1, cat2, cat3);
            URL url = new URL(urlstr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"))) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new FilterServiceException("필터링된 장소를 가져오는 중 오류가 발생하였습니다.", e);
        }
    }

    private String variableUrl(String areaCode, String contentTypeId, String cat1, String cat2, String cat3) {
        return "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?" +
                "numOfRows=300" +
                "&MobileOS=ETC" +
                "&MobileApp=TripBridge" +
                "&areaCode=" + areaCode +
                "&contentTypeId=" + contentTypeId +
                "&cat1=" + cat1 +
                "&cat2=" + cat2 +
                "&cat3=" + cat3 +
                "&serviceKey=YGF0y1fe6pkkysTM1WzMH9htWqLp7iiY2fuy%2BJiEFoI%2BKH%2BON1EnErgGxvE6T2Z5awLIIAlQcuD9hjylPI6GYg%3D%3D" +
                "&_type=json";

    }
}
