package com.xkball.vista_railway.network.packets;

import com.google.common.base.Charsets;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.GCPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class RequestCatenaryDataPacket implements GCPacket {
    
    private String data = "";
    
    public RequestCatenaryDataPacket(){
        if(CatenaryDataManager.INSTANCE.init){
            data = CatenaryDataManager.INSTANCE.data.get();
        }
    }
    
    @SuppressWarnings("unused")
    public RequestCatenaryDataPacket(ByteBuf byteBuf){
        var length = byteBuf.readInt();
        data = byteBuf.readCharSequence(length, Charsets.UTF_8).toString();
    }
    
    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(data.length());
        out.writeCharSequence(data,Charsets.UTF_8);
    }
    
    @Override
    public void onServer(EntityPlayerMP player) {
        GCNetworkManager.INSTANCE.sendPacketToPlayer(new RequestCatenaryDataPacket(),player);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onClient(EntityPlayer player) {
        if(!CatenaryDataManager.INSTANCE.init){
            CatenaryDataManager.INSTANCE.loadFromString(data);
            //Objects.requireNonNull(ConfigurableOverlay.BINDER.getRow("catenary_style")).rebuild(ConfigurableOverlay.STYLE_LIST.get());
        }
    }
}
