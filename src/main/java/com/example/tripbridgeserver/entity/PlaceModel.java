package com.example.tripbridgeserver.entity;

import lombok.Getter;
import lombok.Setter;

public class PlaceModel {

    @Getter
    @Setter
    static public class PlaceRequest{
        private String numofRows;
        private String MobileOS;
        private String MobileAPP;
        private String areaCode;
        private String contentTypeId;
        private String cat1;
        private String cat2;
        private String cat3;
        private String _type;
        private String serviceKey;

    }
    @Getter
    @Setter
    static public class PlaceResponse{
        private PlaceResult placeResult;
    }

    @Getter
    @Setter
    static public class PlaceResult{
        //
    }

    @Getter
    @Setter
    static public class Place{

        private String addr1;
        private String addr2;
        private String areacode;
        private String booktour;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentid;
        private String contenttypeid;
        private String createdtime;
        private String firstiamge;
        private String firstimage2;
        private String cpyhtDivCd;
        private String mapx;
        private String mapy;
        private String mlevel;
        private String modifiedtime;
        private String signungucode;
        private String tel;
        private String title;
        private String zipcode;
    }

}
