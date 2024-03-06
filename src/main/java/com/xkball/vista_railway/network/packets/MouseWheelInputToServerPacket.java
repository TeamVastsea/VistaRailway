package com.xkball.vista_railway.network.packets;

import com.xkball.vista_railway.api.item.IMouseWheelRespond;
import com.xkball.vista_railway.network.GCPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;

public class MouseWheelInputToServerPacket implements GCPacket {
    
    protected int dWheel;
    protected ItemStack itemInHand;
    
    public MouseWheelInputToServerPacket(int dWheel, ItemStack itemInHand) {
        this.dWheel = dWheel;
        this.itemInHand = itemInHand;
    }
    
    @SuppressWarnings("unused")
    public MouseWheelInputToServerPacket(ByteBuf in){
        this.dWheel = in.readInt();
        this.itemInHand = ByteBufUtils.readItemStack(in);
    }
    
    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(dWheel);
        ByteBufUtils.writeItemStack(out,itemInHand);
    }
    
    @Override
    public void onServer(EntityPlayerMP player) {
        var itemInHandServer = player.getHeldItemMainhand();
        if(itemInHandServer.isItemEqualIgnoreDurability(itemInHand) && itemInHandServer.getItem() instanceof IMouseWheelRespond respond){
            respond.respondToMouseWheel(player,itemInHandServer,dWheel);
        }
    }
    
}
