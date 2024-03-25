package com.xkball.vista_railway.utils.func;

import com.xkball.vista_railway.utils.Vector3f;

@FunctionalInterface
public interface LineCreator {
    FloatFunction<Vector3f> createLine(Vector3f start,Vector3f end);
}
