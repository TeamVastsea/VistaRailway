package com.xkball.vista_railway.common.te;

import com.xkball.vista_railway.common.data.PoleTEData;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PoleTE extends VRBaseTE {

    public PoleTEData data = new PoleTEData();
    public PoleTE(){
    
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        data.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        var result = super.writeToNBT(compound);
        return data.writeToNBT(result);
    }
}
