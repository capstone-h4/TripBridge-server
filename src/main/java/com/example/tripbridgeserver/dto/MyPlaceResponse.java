package com.example.tripbridgeserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPlaceResponse {
    private Long id;
    private String place;
    private String address;
    private Long routeOrder;

    public MyPlaceResponse(Long id, String place, String address, Long routeOrder) {
        this.id = id;
        this.place = place;
        this.address = address;
        this.routeOrder=routeOrder;
    }
}
