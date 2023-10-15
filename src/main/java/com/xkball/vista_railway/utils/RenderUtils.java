package com.xkball.vista_railway.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class RenderUtils {

    //逆时针
    public static void rotate(Vector3f self, EnumFacing.Axis axis,int degree){
        rotate(self,axis,Math.toRadians(degree));
    }
    
    public static void rotate(Vector3f self, EnumFacing.Axis axis,double radian){
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
//        for(int i = 0; i < numSlots && i < slotChecksPerTick; ++i) {
//            int slot = this.nextSlot(numSlots);
//            ItemStack item = inventory.extractItem(slot, maxExtracted, true);
//            if (Prep.isValid(item)) {
//                if (this.doTransfer(inventory, item, slot)) {
//                    if (inventory.getStackInSlot(slot).func_190926_b()) {
//                        this.setNextStartingSlot(slot + 1);
//                    } else {
//                        this.setNextStartingSlot(slot);
//                    }
//
//                    return true;
//                }
//            } else {
//                ++slotChecksPerTick;
//            }
//        }
    }
    
    //v1.x>v2.x
    public static float getK(Vector2f v1,Vector2f v2){
        assert v1.x > v2.x;
        //var kv = Vector2f.sub(v1,v2,null);
        //return kv.getY()/kv.getX();
        return (v1.y-v2.y)/(v1.x-v2.x);
    }
    
    public static Vector3f vec3fFromPosMid(BlockPos pos){
        return new Vector3f(pos.getX()+0.5F,pos.getY()+0.5F,pos.getZ()+0.5F);
    }
    
    public static Vector2f midPoint(Vector2f p1,Vector2f p2){
        return new Vector2f((p1.x+p2.x)/2,(p1.y+p2.y)/2);
    }
    
}


