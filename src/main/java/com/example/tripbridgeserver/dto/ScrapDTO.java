package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ScrapDTO {
    private String place;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;


    public Scrap toEntity(UserEntity currentUser){
        Scrap scrap = new Scrap();
        scrap.setPlace(this.place);
        scrap.setAddress(this.address);
        scrap.setLongitude(this.longitude);
        scrap.setLatitude(this.latitude);
        scrap.setUserEntity(currentUser);
        return scrap;
    }
}
