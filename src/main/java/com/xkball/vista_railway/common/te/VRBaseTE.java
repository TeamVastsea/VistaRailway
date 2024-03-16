package com.xkball.vista_railway.common.te;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VRBaseTE extends TileEntity implements ITickable {
    protected boolean firstTick = true;
    public static Consumer<VRBaseTE> SAVE = VRBaseTE::getUpdateTag;
    
    protected NBTTagCompound updateTag  = new NBTTagCompound();
    
    @Override
    public void update() {
        if(firstTick){
            firstTick = false;
            if(!world.isRemote){
                this.scheduleRenderUpdate();
            }
            
        }
    }
    
    public void scheduleRenderUpdate() {
        BlockPos pos = this.pos;
        this.world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }
    
    public void sentDataToClient(Consumer<VRBaseTE> task){
        task.accept(this);
        @SuppressWarnings("deprecation")
        IBlockState blockState = getBlockType().getStateFromMeta(getBlockMetadata());
        world.notifyBlockUpdate(getPos(), blockState, blockState, 0);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
    
    //super就是readFromNBT()
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        var tag = getUpdateTag();
        tag.setTag("updateTag",updateTag.copy());
        var result = new SPacketUpdateTileEntity(this.getPos(),0,tag);
        updateTag = new NBTTagCompound();
        return result;
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
    
 
}
