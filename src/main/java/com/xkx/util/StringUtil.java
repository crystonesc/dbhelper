package com.xkx.util;

public class StringUtil {

    public static  String chnageFirstLetterToUpperCase(String str){
        return  str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
