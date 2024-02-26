package com.xkball.vista_railway.utils;

public class Final<T> {
    private boolean write = false;
    private T value;
    
    public Final(T value){
        this.value = value;
        write = true;
    }
    
    public Final(){
    
    }
    
    public Final<T> set(T value){
        if(!write){
            this.value = value;
            write = true;
        }
        return this;
    }
    
    public T get(){
        return value;
    }
}
