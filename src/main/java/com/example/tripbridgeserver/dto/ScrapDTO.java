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
public class ScrapDTO { // 장소 저장 시 이용 객체
    private String place;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public Scrap toEntity(User currentUser){
        Scrap scrap = new Scrap();
        scrap.setPlace(this.place);
        scrap.setAddress(this.address);
        scrap.setLongitude(this.longitude);
        scrap.setLatitude(this.latitude);
        scrap.setUser(currentUser);
        return scrap;
    }
}
