package com.xkball.vista_railway.utils;

import com.xkball.vista_railway.utils.func.FloatFunction;
import com.xkball.vista_railway.utils.func.LineCreator;

public class MathUtils {
    public static final Vector3f ORIGIN = new Vector3f(0,0,0);
    
    public static class LineFormat {
        public static final LineFormat None = new LineFormat(false,0.00f,(vs,ve) -> (f) -> MathUtils.ORIGIN,(f) -> 0);
        public final boolean enable;
        public final float radius;
        public final LineCreator offset;
        public final FloatFunction<Integer> samples;
        
        public LineFormat(boolean enable, float radius, LineCreator offset, FloatFunction<Integer> samples) {
            this.enable = enable;
            this.radius = radius;
            this.offset = offset;
            this.samples = samples;
        }
        public LineFormat(boolean enable, float radius, LineCreator offset) {
            this.enable = enable;
            this.radius = radius;
            this.offset = offset;
            this.samples = (f) -> 5;
        }
    }
}
