package com.xkball.vista_railway.common.te;

import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.data.CatenaryRenderData;
import com.xkball.vista_railway.common.data.PoleTEData;
import com.xkball.vista_railway.utils.MathUtilsClient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;


import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PoleTE extends VRBaseTE {

    public PoleTEData data = new PoleTEData();
    public CatenaryRenderData renderData = new CatenaryRenderData();
    public final Set<BlockPos> needNotify = new HashSet<>();
    public PoleTE(){
    
    }
    
    public boolean hasStyle(){
        return CatenaryDataManager.INSTANCE.get(data.styleID) != null;
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
        renderData.needUpdate = true;
        if(this.hasWorld() ){
            if(this.getWorld().isRemote && !needNotify.isEmpty()){
                for (BlockPos blockPos : needNotify) {
                    var te = this.getWorld().getTileEntity(blockPos);
                    if(te instanceof PoleTE poleTE){
                        poleTE.renderData.needUpdate = true;
                    }
                }
            }
            else if(!this.getWorld().isRemote){
                renderData.updateServer(this);
            }
        }
    }
    
    @Override
    public void markDirty() {
        if(!this.getWorld().isRemote){
            renderData.updateServer(this);
        }
        super.markDirty();
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
        this.sentDataToClient(SAVE);
    }
    
    public void setOffset(com.xkball.vista_railway.utils.Vector3f offset){
        data.offset = offset;
        this.markDirty();
        this.sentDataToClient(SAVE);
    }
    
    @Nullable
    @SideOnly(Side.CLIENT)
    public Vector3f getOffsetTop(int nodeID){
        var offset = this.data.offset.toVec3f().translate(this.getPos().getX(),this.getPos().getY(),this.getPos().getZ());
        var nodeMap = CatenaryDataManager.INSTANCE.get(data.styleID).nodeMap();
        if(nodeMap.get(nodeID) == null) return null;
        return Vector3f.add(offset, MathUtilsClient.rotate(nodeMap.get(nodeID).topOffset().toVec3f(), EnumFacing.Axis.Y, data.yRotation),offset);
    }
    
    @Nullable
    @SideOnly(Side.CLIENT)
    public Vector3f getOffsetBottom(int nodeID){
        var offset = this.data.offset.toVec3f().translate(this.getPos().getX(),this.getPos().getY(),this.getPos().getZ());
        var nodeMap = CatenaryDataManager.INSTANCE.get(data.styleID).nodeMap();
        if(nodeMap.get(nodeID) == null) return null;
        return Vector3f.add(offset, MathUtilsClient.rotate(nodeMap.get(nodeID).bottomOffset().toVec3f(), EnumFacing.Axis.Y, data.yRotation),offset);
    }
    
    @Override
    public double getMaxRenderDistanceSquared() {
        return 1000000d;
    }
}
