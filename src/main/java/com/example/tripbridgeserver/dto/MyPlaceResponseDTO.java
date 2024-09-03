package com.example.tripbridgeserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPlaceResponseDTO {
    private Long id;
    private String place;
    private String address;
    private Long route_order;

    public MyPlaceResponseDTO(Long id, String place, String address, Long route_order) {
        this.id = id;
        this.place = place;
        this.address = address;
        this.route_order=route_order;
    }
}
