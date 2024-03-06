package com.xkball.vista_railway.common.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.util.vector.Vector3f;

import static com.xkball.vista_railway.utils.NBTUtils.readBlockPos;
import static com.xkball.vista_railway.utils.NBTUtils.writeBlockPos;


public class PoleTEData {
    public int styleID = -1;
    public Vector3f offset = new Vector3f(0,0,0);
    public BlockPos[] connections = new BlockPos[]{BlockPos.ORIGIN,BlockPos.ORIGIN,BlockPos.ORIGIN,BlockPos.ORIGIN};
    public boolean[] relativePos = new boolean[]{true,true,true,true};
    public int yRotation;
    
    public void readFromNBT(NBTTagCompound compound) {
        
        var ox = compound.getFloat("offset_x");
        var oy = compound.getFloat("offset_y");
        var oz = compound.getFloat("offset_z");
        offset.set(ox,oy,oz);
        for(int i = 0;i<connections.length;i++){
            connections[i] = readBlockPos(compound,"connection_"+i);
            relativePos[i] = !compound.hasKey("relative_" + i) || compound.getBoolean("relative_" + i);
        }
        styleID = compound.hasKey("style_id")?compound.getInteger("style_id"):-1;
        yRotation = compound.hasKey("y_rotation")?compound.getInteger("y_rotation"):0;
        
    }
    
    
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setFloat("offset_x",offset.x);
        compound.setFloat("offset_y",offset.y);
        compound.setFloat("offset_z",offset.z);
        for(int i = 0;i<connections.length;i++){
            writeBlockPos(compound,"connection_"+i,connections[i]);
            compound.setBoolean("relative_" + i,relativePos[i]);
        }
        compound.setInteger("style_id",styleID);
        compound.setInteger("y_rotation",yRotation);
        return compound;
    }
    
}
