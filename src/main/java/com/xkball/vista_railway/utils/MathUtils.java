package com.xkball.vista_railway.utils;

public class MathUtils {
    
    public static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }
    
}
