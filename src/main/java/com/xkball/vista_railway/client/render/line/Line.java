package com.xkball.vista_railway.client.render.line;

import com.xkball.vista_railway.utils.function.FloatUnaryOperator;
import org.lwjgl.util.vector.Vector3f;


public class Line {
    
    private final Vector3f start;
    private final Vector3f end;
    private final double length;
    
    private final FloatUnaryOperator fx;
    private final FloatUnaryOperator derivative_fx;
    
    private final Node[] nodes;
    
    public Line(Vector3f start, Vector3f end, int steps, FloatUnaryOperator fx, FloatUnaryOperator derivative_fx){
        this.start = start;
        this.end = end;
        this.length = Vector3f.sub(end,start,null).length();
        nodes = new Node[steps+1];
        this.fx = fx;
        this.derivative_fx = derivative_fx;
    }
}
