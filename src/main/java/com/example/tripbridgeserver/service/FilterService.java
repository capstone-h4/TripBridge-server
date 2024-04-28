//package com.example.tripbridgeserver.service;
//
//import com.example.tripbridgeserver.entity.PlaceModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//
//@Service
//public class FilterService {
//    @Autowired
//    private PlaceFilter placefilter;
//
//    public HashMap<String, String> SearchPlace(String target){
//        PlaceModel.PlaceRequest placeRequest = new PlaceModel.PlaceRequest();
//        PlaceModel.PlaceResponse placeResponse = null;
//
//        HashMap<String, String> result = new HashMap<String, String>();
//        try{
//            placeRequest.setAreaCode("1");
//            placeResponse = placefilter.SearchPlaceList(placeRequest);
//
//            result.put("PlaceList", PlaceListCard(placeResponse));
//
//        }catch (Exception E){
//
//        }
//        return result;
//    }
//
//    public String PlaceListCard(PlaceModel.PlaceResponse placeResponse){
//        StringBuffer placeListCard = new StringBuffer();
//
//        //for(PlaceModel.Place place : placeResponse.getPlaceResult().);
//
//        return placeListCard.toString();
//    }
//
//
//}
