package com.xkball.vista_railway.utils;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class AssertUtils {
    public static boolean assertIsInt(String number){
        try {
            Integer.parseInt(number);
        }catch (NumberFormatException exception){
            return false;
        }
        return true;
    }
    
    public static <T> T requireNonNullElseGet(T obj, @Nonnull Supplier<T> supplier){
        return obj==null? Objects.requireNonNull(supplier.get()):obj;
    }
}
