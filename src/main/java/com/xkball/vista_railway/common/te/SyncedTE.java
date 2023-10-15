package com.xkball.vista_railway.common.te;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.function.Consumer;

//from https://github.com/GregTechCEu/GregTech/blob/master/src/main/java/gregtech/api/metatileentity/SyncedTileEntityBase.java under LGPL-3.0 license.
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SyncedTE extends TileEntity {
    protected final Int2ObjectMap<byte[]> updates = new Int2ObjectArrayMap<>(5);
    
    public void writeInitialSyncData(PacketBuffer var1){}
    
    public void receiveInitialSyncData(PacketBuffer var1){}
    
    public void receiveCustomData(int var1, PacketBuffer var2){}
    
    public void writeCustomData(int discriminator, Consumer<PacketBuffer> dataWriter) {
        ByteBuf backedBuffer = Unpooled.buffer();
        dataWriter.accept(new PacketBuffer(backedBuffer));
        byte[] updateData = Arrays.copyOfRange(backedBuffer.array(), 0, backedBuffer.writerIndex());
        updates.put(discriminator, updateData);
        @SuppressWarnings("deprecation")
        IBlockState blockState = getBlockType().getStateFromMeta(getBlockMetadata());
        world.notifyBlockUpdate(getPos(), blockState, blockState, 0);
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        if (this.updates.isEmpty()) {
            return null;
        }
        NBTTagCompound updateTag = new NBTTagCompound();
        NBTTagList listTag = new NBTTagList();
        for (Int2ObjectMap.Entry<byte[]> entry : updates.int2ObjectEntrySet()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setByteArray(Integer.toString(entry.getIntKey()), entry.getValue());
            listTag.appendTag(entryTag);
        }
        updateTag.setTag("d", listTag);
        this.updates.clear();
        return new SPacketUpdateTileEntity(getPos(), 0, updateTag);
    }
    
    @Override
    public void onDataPacket(@Nonnull NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTag = pkt.getNbtCompound();
        NBTTagList listTag = updateTag.getTagList("d", Constants.NBT.TAG_COMPOUND);
        for (NBTBase entryBase : listTag) {
            NBTTagCompound entryTag = (NBTTagCompound) entryBase;
            for (String discriminatorKey : entryTag.getKeySet()) {
                ByteBuf backedBuffer = Unpooled.copiedBuffer(entryTag.getByteArray(discriminatorKey));
                receiveCustomData(Integer.parseInt(discriminatorKey), new PacketBuffer(backedBuffer));
            }
        }
    }
    
    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound updateTag = super.getUpdateTag();
        ByteBuf backedBuffer = Unpooled.buffer();
        writeInitialSyncData(new PacketBuffer(backedBuffer));
        byte[] updateData = Arrays.copyOfRange(backedBuffer.array(), 0, backedBuffer.writerIndex());
        updateTag.setByteArray("d", updateData);
        return updateTag;
    }
    
    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        super.readFromNBT(tag);
        byte[] updateData = tag.getByteArray("d");
        ByteBuf backedBuffer = Unpooled.copiedBuffer(updateData);
        receiveInitialSyncData(new PacketBuffer(backedBuffer));
    }
    public void scheduleRenderUpdate() {
        BlockPos pos = this.pos;
        this.world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("saved"))
            handleUpdateTag(compound.getCompoundTag("saved"));
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("saved",getUpdateTag());
        return compound;
    }
}
