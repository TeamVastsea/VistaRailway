package com.xkball.vista_railway.utils;

public class AssertUtils {
    public static boolean assertIsInt(String number){
        try {
            Integer.parseInt(number);
        }catch (NumberFormatException exception){
            return false;
        }
        return true;
    }
}
