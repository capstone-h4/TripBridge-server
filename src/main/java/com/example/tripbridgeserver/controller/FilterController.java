package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.service.FilterService;
import com.example.tripbridgeserver.service.FilterServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {

    private final FilterService filterService;
    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    // 공공데이터 이용 장소 필터링
    @GetMapping("/place/list")
    public ResponseEntity<String> showPlace(
            @RequestParam("areaCode") String areaCode,
            @RequestParam("contentTypeId") String contentTypeId,
            @RequestParam("cat1") String cat1,
            @RequestParam("cat2") String cat2,
            @RequestParam(value = "cat3", required = false, defaultValue = "") String cat3
    ){
        try {
            String result = filterService.getFilteredPlaces(areaCode, contentTypeId, cat1, cat2, cat3);
            return ResponseEntity.ok(result);
        } catch (FilterServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터를 가져오는 중 오류가 발생하였습니다.");
        }
    }

    // 장소 상세정보 조회
    @GetMapping("/place")
    public ResponseEntity<String> showPlaceInfo(
            @RequestParam("contentTypeId") String contentTypeId,
            @RequestParam("contentId") String contentId
    ){
        try {
            String result = filterService.getDetailedPlaceInfo(contentTypeId, contentId);
            return ResponseEntity.ok(result);
        } catch (FilterServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터를 가져오는 중 오류가 발생하였습니다.");
        }
    }
}
