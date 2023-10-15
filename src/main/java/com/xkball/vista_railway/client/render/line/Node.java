package com.xkball.vista_railway.client.render.line;


import com.xkball.vista_railway.utils.RenderUtils;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;

@SuppressWarnings("UnusedReturnValue")
public class Node {
    static final float halfSquareRootThree = 0.8660254037f;
    
    public static final Vector2f UNIT = new Vector2f(1,0);
    public static final Vector2f UNITY = new Vector2f(0,1);
    public static final float halfCircleRadian = (float) Math.toRadians(180);
    public static final float quarterCircleRadian = (float) Math.toRadians(90);
    
    private final Vector3f vec_1 = new Vector3f(0,0,1);
    private final Vector3f vec_2 = new Vector3f(0,-halfSquareRootThree,0.5f);
    private final Vector3f vec_3 = new Vector3f(0,-halfSquareRootThree,-0.5f);
    private final Vector3f vec_4 = new Vector3f(0,0,-1);
    private final Vector3f vec_5 = new Vector3f(0,halfSquareRootThree,-0.5f);
    private final Vector3f vec_6 = new Vector3f(0,halfSquareRootThree,0.5f);
    
    private final Vector2f point = new Vector2f(0,0);
    
    public final Vector3f[] pos = new Vector3f[6];
    
    public final Vector3f[] normal = new Vector3f[]{vec_1,vec_2,vec_3,vec_4,vec_5,vec_6};
    
    public Node(){}
    
    public Node(float x,float y){
        this.setX(x);
        this.setY(y);
    }
    
    public static Node withX(float x){
        return new Node().setX(x);
    }
    
    public Node setX(float x){
//        for(var c : vector3fs){
//            c.setX(x);
//        }
        point.setX(x);
        return this;
    }
    
    public Node setY(float y){
        point.setY(y);
        return this;
    }
    
    public Node rotateAndCopy(Vector2f to){
        var result = copyWithoutPoint();
        var radian = Vector2f.angle(to,UNIT);
        radian = radian+quarterCircleRadian;
        if(to.y<0) radian = radian+halfCircleRadian;
        for(var c:result.normal){
            RenderUtils.rotate(c, EnumFacing.Axis.Z,radian);
        }
        //把result改成this有惊喜
        return result;
    }
    
    public Node copyWithoutPoint(){
        var result = new Node();
        for(int i = 0; i< normal.length; i++){
            result.normal[i].set(this.normal[i]);
        }
        return result;
    }
    
    public void computeFinalPoint(@Nullable Node left,@Nullable Node right){
        var x = point.x;
        var y = point.y;
        if(left != null && right != null){
//            float kLeft = RenderUtils.getK(this.point,left.point);
//            float kRight = RenderUtils.getK(right.point,this.point);
//            if(kRight == kLeft && kLeft == 0) return;
//            var selfLeft = rotateAndCopy(new Vector2f(x-left.point.x,y-left.point.y));
//            var selfRight = rotateAndCopy(new Vector2f(right.point.x-x,right.point.y-y));
//
//            for(int i=0;i<normal.length;i++){
//                var x_l = x+selfLeft.normal[i].x;
//                var x_r = x+selfRight.normal[i].x;
//                var y_l = y+selfLeft.normal[i].y;
//                var y_r = y+selfRight.normal[i].y;
//
//                var b_l = y_l-kLeft*x_l;
//                var b_r = y_r-kRight*x_r;
//
//                var x_f = (b_r-b_l)/(kLeft-kRight);
//                var y_f = kLeft*x_f+b_l;
//                normal[i].setX(x_f);
//                normal[i].setY(y_f);
//            }
            var tol = new Vector2f(x-left.point.x,y-left.point.y);
            var tor = new Vector2f(right.point.x-x,right.point.y-y);
            var radian1 = Vector2f.angle(tol,UNIT);
            radian1 = tol.y>0?radian1: -radian1;
            var radian2 = Vector2f.angle(tol,tor);
            //tol = Vector2f.add(tol,tor,tol);
           // var radian2 = Vector2f.angle(UNIT,tor);
//            var d1 = Math.toDegrees(radian1);
//            var d2 = Math.toDegrees(radian2);
//            var r = Vector2f.angle(tol,UNITY);
            for(var c:normal){
                RenderUtils.rotate(c, EnumFacing.Axis.Z,radian2/2+radian1/2);
            }
            
        }
//        else if(left != null){
//            var to = new Vector2f(x-left.point.x,y-left.point.y);
//            var radian = Vector2f.angle(to,UNIT);
//            radian = radian+quarterCircleRadian;
//            if(to.y<0) radian = radian+halfCircleRadian;
//            for(var c:normal){
//                RenderUtils.rotate(c, EnumFacing.Axis.Z,radian);
//            }
//        }
//        else if(right != null){
//            var to = new Vector2f(right.point.x-x,right.point.y-y);
//            var radian = Vector2f.angle(to,UNIT);
//            radian = radian+quarterCircleRadian;
//            if(to.y<0) radian = radian+halfCircleRadian;
//            for(var c:normal){
//                RenderUtils.rotate(c, EnumFacing.Axis.Z,radian);
//            }
//        }
    }
   
    public void end(){
         point.setX(point.x*(1/0.08f));
         point.setY(point.y*(1/0.08f));
        var pos3 = new Vector3f(point.x,point.y,0);
        for(int i=0;i< pos.length;i++){
           // normal[i] = normal[i].normalise(normal[i]);
            pos[i] = Vector3f.add(normal[i],pos3,null);
            //pos[i] = pos[i].normalise(pos[i]);
        }
    }
    
}
