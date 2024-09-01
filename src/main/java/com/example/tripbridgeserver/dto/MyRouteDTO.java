package com.example.tripbridgeserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRouteDTO {
    private Long id;
    private String name;
    private int rate;
    private String comment;

    public MyRouteDTO(Long id, String name, int rate, String comment){
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.comment = comment;
    }

}
