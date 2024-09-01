package com.example.tripbridgeserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyRouteResponseDTO {

    private Long id;
    private String name;
    private int rate;
    private String comment;
    private List<MyPlaceResponseDTO> myPlaces;

    public MyRouteResponseDTO(Long id, String name, int rate, String comment, List<MyPlaceResponseDTO> myPlaces) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.comment = comment;
        this.myPlaces = myPlaces;
    }
}
