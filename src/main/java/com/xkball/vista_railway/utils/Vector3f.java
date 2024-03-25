package com.xkball.vista_railway.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Vector3f {
    
    public float x, y, z;
    public Vector3f(){
        this(0,0,0);
    }
    
    public Vector3f(float x, float y, float z) {
        set(x, y, z);
    }
    
    @SideOnly(Side.CLIENT)
    public Vector3f(org.lwjgl.util.vector.Vector3f vector3f){
        set(vector3f);
    }
    
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    //必须要new一个
    @SideOnly(Side.CLIENT)
    public org.lwjgl.util.vector.Vector3f toVec3f(){
        return new org.lwjgl.util.vector.Vector3f(x,y,z);
    }
    
    public Vector3f translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    @SideOnly(Side.CLIENT)
    public void set(org.lwjgl.util.vector.Vector3f vector3f){
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
    }
    
    @Override
    public String toString() {
        return "Vector3f[" + x + ", " + y + ", " + z + ']';
    }
    
    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null)
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        else {
            dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
            return dest;
        }
    }
    
    public float length() {
        return (float) Math.sqrt(x*x+y*y+z*z);
    }
}
