package com.xkball.vista_railway.utils;

import com.xkball.vista_railway.utils.func.FloatUnaryOperator;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

@SideOnly(Side.CLIENT)
public class MathUtilsClient {
    
    public static final Vector2f OX = new Vector2f(1,0);

    
    public static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }
    
    public static FloatUnaryOperator createCatenary(float a,float scale){
        FloatUnaryOperator raw = (f) -> {
            float x = f-0.5f;
            x*=scale;
            return (float) (a*Math.cosh(x/a))-a;
        };
        var d = raw.applyAsFloat(1);
        return (f) -> raw.applyAsFloat(f)-d;
    }
    
//    public static Vector3f rotateVec3XOZ(Vector3f raw, Vector3f dest, float rotationRadians){
//        if(rotationRadians == 0 || raw.length() <= 0.001) return dest.set(raw);
//        var destXOZ = new Vector2f(raw.x, raw.z);
//        var l = Math.sqrt(destXOZ.length());
//        var r = Vector2f.angle(destXOZ,OX);
//        r = raw.z>0?r:-r;
//        r += rotationRadians;
//        dest.set((float) (Math.cos(r) * l),raw.y, (float) (Math.sin(r) * l));
//        return dest;
//    }
    
    //逆时针
    public static Vector3f rotate(Vector3f self, EnumFacing.Axis axis,int degree){
        return rotate(self,axis,Math.toRadians(degree));
    }
    
    public static Vector3f rotate(Vector3f self, EnumFacing.Axis axis, double radian){
        var x = self.getX();
        var y = self.getY();
        var z = self.getZ();
        var sinTheta = (float) Math.sin(radian);
        var cosTheta = (float) Math.cos(radian);
        switch (axis){
            case X -> {
                self.setY(y*cosTheta+z*sinTheta);
                self.setZ(z*cosTheta-y*sinTheta);
            }
            case Y -> {
                self.setX(x*cosTheta+z*sinTheta);
                self.setZ(z*cosTheta-x*sinTheta);
            }
            case Z-> {
                self.setX(x*cosTheta-y*sinTheta);
                self.setY(y*cosTheta+x*sinTheta);
            }
        }
        return self;
    }
    
}
