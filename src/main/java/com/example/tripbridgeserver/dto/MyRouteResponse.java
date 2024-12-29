package com.example.tripbridgeserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyRouteResponse {

    private Long id;
    private String name;
    private int rate;
    private String comment;
    private List<MyPlaceResponse> myPlaces;

    public MyRouteResponse(Long id, String name, int rate, String comment, List<MyPlaceResponse> myPlaces) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.comment = comment;
        this.myPlaces = myPlaces;
    }
}
