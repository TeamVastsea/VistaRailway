package com.xkball.vista_railway.common.data;

import com.xkball.vista_railway.utils.MathUtils;
import com.xkball.vista_railway.utils.MathUtilsClient;
import com.xkball.vista_railway.utils.Vector3f;

import static com.xkball.vista_railway.utils.MathUtils.LineFormat.None;

public enum LineType {
    T1(new MathUtils.LineFormat(true,0.02f,(vs,ve) -> {
        var l = Vector3f.sub(vs,ve,new Vector3f()).length();
        return MathUtilsClient.createCatenary(5,l/6).ascensionAsY();
    }, (l) -> (int) (l*4)),new MathUtils.LineFormat(true,0.02f,(vs, ve) -> (f) -> MathUtils.ORIGIN)),
    T2(new MathUtils.LineFormat(true,0.04f,(vs,ve) -> (f) -> MathUtils.ORIGIN),new MathUtils.LineFormat(true,0.04f,(vs,ve) -> (f) -> MathUtils.ORIGIN)),
    t3(new MathUtils.LineFormat(true,0.02f,(vs,ve) -> (f) -> MathUtils.ORIGIN),new MathUtils.LineFormat(true,0.02f,(vs,ve) -> (f) -> MathUtils.ORIGIN)),
    T4(None,new MathUtils.LineFormat(true,0.04f,(vs,ve) -> (f) -> MathUtils.ORIGIN)),
    T5(None,new MathUtils.LineFormat(true,0.02f,(vs,ve) -> {
        var l = Vector3f.sub(vs,ve,new Vector3f()).length();
        return MathUtilsClient.createCatenary(5,l/6).ascensionAsY();
    }, (l) -> (int) (l*4))),
    T6(None,new MathUtils.LineFormat(true,0.02f,(vs,ve) -> {
        var l = Vector3f.sub(vs,ve,new Vector3f()).length();
        return MathUtilsClient.createCatenary(5,l/6).ascensionAsY();
    }, (l) -> (int) (l*4)));
    private final MathUtils.LineFormat top;
    private final MathUtils.LineFormat bottom;
    
    
    LineType(MathUtils.LineFormat top, MathUtils.LineFormat bottom) {
        this.top = top;
        this.bottom = bottom;
    }
    
    public static LineType getFromID(int id){
        //未检查索引
        return values()[id-1];
    }
    
    
    public MathUtils.LineFormat getBottom() {
        return bottom;
    }
    
    public MathUtils.LineFormat getTop() {
        return top;
    }
}
