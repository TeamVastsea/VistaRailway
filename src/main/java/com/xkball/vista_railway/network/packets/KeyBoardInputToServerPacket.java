package com.xkball.vista_railway.network.packets;

import com.xkball.vista_railway.api.item.IKeyBoardInputRespond;
import com.xkball.vista_railway.network.GCPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;

public class KeyBoardInputToServerPacket implements GCPacket {
    protected int key;
    protected ItemStack itemInHand;
    
    public KeyBoardInputToServerPacket(int key,ItemStack itemInHand){
        this.key = key;
        this.itemInHand = itemInHand;
    }
    
    @SuppressWarnings("unused")
    public KeyBoardInputToServerPacket(ByteBuf in){
        this.key = in.readInt();
        this.itemInHand = ByteBufUtils.readItemStack(in);
    }
    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(key);
        ByteBufUtils.writeItemStack(out,itemInHand);
    }
    
    @Override
    public void onServer(EntityPlayerMP player) {
        var itemInHandServer = player.getHeldItemMainhand();
        if(itemInHandServer.isItemEqualIgnoreDurability(itemInHand) && itemInHandServer.getItem() instanceof IKeyBoardInputRespond respond){
            respond.onKeyPressed(itemInHandServer,key);
        }
    }
}
