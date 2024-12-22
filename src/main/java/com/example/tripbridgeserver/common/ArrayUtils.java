package com.example.tripbridgeserver.common;

import java.util.List;

public class ArrayUtils {

    public static boolean isEmpty(List<?> list){
        if(list == null || list.isEmpty()) return true;
        return false;
    }
}
