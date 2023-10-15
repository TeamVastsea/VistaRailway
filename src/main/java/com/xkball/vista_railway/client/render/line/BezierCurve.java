package com.xkball.vista_railway.client.render.line;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.LinkedList;

public class BezierCurve {
    public final int max_depth;
    public final Vector2f point1,point2,point3,point4;
    
    public BezierCurve(int maxDepth, Vector2f point1, Vector2f point2, Vector2f point3, Vector2f point4) {
        this.max_depth = maxDepth;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
    }
    
    public BezierCurve(int maxDepth, Vector3f point1, Vector3f point2, Vector3f point3, Vector3f point4) {
        this.max_depth = maxDepth;
        this.point1 = new Vector2f(0,0);
        
        var vec1 = Vector3f.sub(point1,point4,null);
        var length1 = vec1.length();
        this.point4 = new Vector2f(length1,0);
        
        var vec2 = Vector3f.sub(point1,point2,null);
        var length2 = vec2.length();
        var radian2 = Vector3f.angle(vec2,vec1);
        this.point2 = new Vector2f((float) (length2*Math.cos(radian2)), (float) (length2*Math.sin(radian2)));
        
        var vec3 = Vector3f.sub(point1,point3,null);
        var length3 = vec3.length();
        var radian3 = Vector3f.angle(vec3,vec1);
        this.point3 = new Vector2f((float) (length3*Math.cos(radian3)), (float) (length3*Math.sin(radian3)));
     
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    public Node[] getPoints(){
        var result = new LinkedList<Node>();
        //getPointsInternal(points,point1,point2,point3,point4,0);
        for(int i = 0;i<max_depth;i++) {
            double t = ((double) i)/((double) max_depth);
            result.add(new Node(
                    bezierAt(t, point1.x,point2.x,point3.x,point4.x),
                    bezierAt(t,point1.y,point2.y,point3.y,point4.y)));
            
        }
        return result.toArray(new Node[0]);
    }
    
    public static float bezierAt(double t,double x1,double x2,double x3,double x4){
        double _t = 1-t;
        double x0 = x1*_t*_t*_t;
        x0 = x0 + x2*_t*_t*t*3;
        x0 = x0 + x3*_t*t*t*3;
        x0 = x0 + x4*t*t*t;
        return (float) x0;
    }
    
//    @SuppressWarnings("UnnecessaryLocalVariable")
//    private void getPointsInternal(LinkedList<Vector2f> points, Vector2f point1, Vector2f point2, Vector2f point3, Vector2f point4, int level){
//        if(level>max_depth){
//            if (points.contains(point1)) {
//                points.add(Math.min(points.indexOf(point1), points.size()),point4);
//            }
//            else {
//                points.add(point1);
//                points.add(point4);
//            }
//        }
//        else {
//            var l1 = point1;
//            var l2 = RenderUtils.midPoint(point1,point2);
//            var h = RenderUtils.midPoint(point2,point3);
//            var r3 = RenderUtils.midPoint(point3,point4);
//            var r4 = point4;
//            var l3 = RenderUtils.midPoint(l2,h);
//            var r2 = RenderUtils.midPoint(r3,h);
//            var l4 = RenderUtils.midPoint(l3,r2);
//            var r1 = l4;
//
//            getPointsInternal(points,l1,l2,l3,l4,level+1);
//            getPointsInternal(points,r1,r2,r3,r4,level+1);
//        }
//    }
    
}
