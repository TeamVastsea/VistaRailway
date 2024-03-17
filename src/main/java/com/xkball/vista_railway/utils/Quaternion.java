package com.xkball.vista_railway.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Quaternion {
    public float x, y, z, w;
    public Quaternion(float x, float y, float z, float w) {
        set(x, y, z, w);
    }
    
    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    @SideOnly(Side.CLIENT)
    public org.lwjgl.util.vector.Quaternion toQuaternion(){
        return new org.lwjgl.util.vector.Quaternion(x,y,z,w);
    }
}
