package com.xkball.vista_railway.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class NBTUtils {
    public static BlockPos readBlockPos(NBTTagCompound compound, String name){
        if(compound.isEmpty()) return BlockPos.ORIGIN;
        assert compound.hasKey(name+"_x") && compound.hasKey(name+"_y") && compound.hasKey(name+"_z");
        var x = compound.getInteger(name+"_x");
        var y = compound.getInteger(name+"_y");
        var z = compound.getInteger(name+"_z");
        return new BlockPos(x,y,z);
    }
    
    public static void writeBlockPos(NBTTagCompound compound,String name,BlockPos pos){
        compound.setInteger(name+"_x",pos.getX());
        compound.setInteger(name+"_y",pos.getY());
        compound.setInteger(name+"_z",pos.getZ());
    }
    
    public static Vector3f readVec3f(NBTTagCompound compound, String name){
        if(compound.isEmpty()) return new Vector3f(0,0,0);
        assert compound.hasKey(name+"_x") && compound.hasKey(name+"_y") && compound.hasKey(name+"_z");
        var x = compound.getFloat(name+"_x");
        var y = compound.getFloat(name+"_y");
        var z = compound.getFloat(name+"_z");
        return new Vector3f(x,y,z);
    }
    
    public static void writeVec3f(NBTTagCompound compound, String name, Vector3f vec3f){
        compound.setFloat(name+"_x",vec3f.x);
        compound.setFloat(name+"_y",vec3f.y);
        compound.setFloat(name+"_z",vec3f.z);
    }
    
    @Nullable
    public static BlockPos readBlockPosOrNull(@Nullable NBTTagCompound compound, String name){
        if (compound == null || !compound.hasKey(name + "_x") || !compound.hasKey(name + "_y") || !compound.hasKey(name+"_z")) {
            return null;
        }
        var x = compound.getInteger(name+"_x");
        var y = compound.getInteger(name+"_y");
        var z = compound.getInteger(name+"_z");
        return new BlockPos(x,y,z);
    }
    
    public static NBTTagCompound getOrCreateTag(ItemStack itemStack){
        return AssertUtils.requireNonNullElseGet(itemStack.getTagCompound(),NBTTagCompound::new);
    }
    
}
