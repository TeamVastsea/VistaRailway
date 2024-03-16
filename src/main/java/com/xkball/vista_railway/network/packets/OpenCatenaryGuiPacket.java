package com.xkball.vista_railway.network.packets;

import com.xkball.vista_railway.client.gui.screen.CatenaryEditScreen;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.GCPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

import static com.xkball.vista_railway.common.te.VRBaseTE.SAVE;

public class OpenCatenaryGuiPacket implements GCPacket {
    private boolean updateServer = false;
    public boolean closeGUI = false;
    private NBTTagCompound data;
    public OpenCatenaryGuiPacket(PoleTE te){
        this.data = te.getUpdateTag();
    }
    
    public OpenCatenaryGuiPacket(NBTTagCompound data){
        this.data = data;
    }
    @SuppressWarnings("unused")
    public OpenCatenaryGuiPacket(ByteBuf byteBuf){
        this.updateServer = byteBuf.readBoolean();
        this.closeGUI = byteBuf.readBoolean();
        this.data = ByteBufUtils.readTag(byteBuf);
    }
    public void setUpdateServer(){
        this.updateServer = true;
    }
    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeBoolean(updateServer);
        out.writeBoolean(closeGUI);
        ByteBufUtils.writeTag(out,data);
    }
    
    @Override
    public void onServer(EntityPlayerMP player) {
        var world = player.world;
        var x = data.getInteger("x");
        var y = data.getInteger("y");
        var z = data.getInteger("z");
        var pos = new BlockPos(x,y,z);
        if(!world.isBlockLoaded(pos)) return;
        var te = world.getTileEntity(pos);
        if(te instanceof PoleTE pe){
            if(updateServer){
                pe.data.readFromNBT(data);
                pe.markDirty();
                pe.sentDataToClient(SAVE);
            }
            if(!closeGUI){
                GCNetworkManager.INSTANCE.sendPacketToPlayer(new OpenCatenaryGuiPacket(pe),player);
            }
        }
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onClient(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new CatenaryEditScreen(data));
    }
}
