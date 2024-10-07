package com.example.tripbridgeserver.entity;

import lombok.Getter;
import lombok.Setter;

public class PlaceModel {
    @Getter
    @Setter
    static public class PlaceResponse {
        private PlaceResult placeResult;
    }

    @Getter
    @Setter
    static public class PlaceResult {
    }
}
