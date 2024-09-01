package com.example.tripbridgeserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPlaceResponseDTO {
    private Long id;
    private String place;
    private String address;

    public MyPlaceResponseDTO(Long id, String place, String address) {
        this.id = id;
        this.place = place;
        this.address = address;
    }
}
