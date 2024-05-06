package com.example.tripbridgeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RouteDTO {
    private String place;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long route_order;

}
