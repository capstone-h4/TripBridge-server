package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ScrapRequest { // 장소 저장 시 이용 객체
    private String place;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public Scrap toEntity(User user){
        return new Scrap(place, address, latitude, longitude, user);
    }
}
