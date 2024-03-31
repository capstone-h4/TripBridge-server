package com.example.tripbridgeserver.common;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    // 리스트에 찾는 값이 있는지 체크
    public static boolean isEmpty(List<?> list){
        if( list == null || list.isEmpty()) return true;
        return false;
    }


    // 리스트에 찾는 값이 있는지 체크
    public static boolean isEmpty(Object[] array){
        if( array == null || array.length == 0) return true;
        return false;
    }

    //리스트에 찾는 값이 있는지 체크
    public static boolean contains(List<?> list, Object o){
        if( isEmpty(list)) return false;
        return list.contains(o);
    }

    // 배열에 찾는 값이 있는지 체크
    public static boolean contains(Object[] array, Object o){
        if( isEmpty(array)) return false;
        return contains(Arrays.asList(array), o);
    }
}
