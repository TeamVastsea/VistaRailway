package com.xkball.vista_railway.common.te;

import com.xkball.vista_railway.common.data.PoleTEData;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
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
    
    public void setNodeConnection(int id,@Nullable BlockPos pos){
        if(pos == null) return;
        if (data.relativePos[id]) {
            data.connections[id] = pos.subtract(this.pos);
        }
        else {
            data.connections[id] = pos;
        }
        this.markDirty();
    }
    
    public void overrideRelativePosSetting(int id,int state){
        if(state == 0)return;
        data.relativePos[id] = state == 1;
        this.markDirty();
    }
    
    public void setOffset(Vector3f offset){
        data.offset = offset;
        this.markDirty();
        this.sentDataToClient(SAVE);
    }
}
