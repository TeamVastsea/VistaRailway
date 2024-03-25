package com.xkball.vista_railway.utils.func;

import com.xkball.vista_railway.utils.Vector3f;

@FunctionalInterface
public interface FloatUnaryOperator {
    
    float applyAsFloat(float f);
    
    default FloatFunction<Vector3f> ascensionAsY(){
        return (f) -> new Vector3f(0,applyAsFloat(f),0);
    }
}
