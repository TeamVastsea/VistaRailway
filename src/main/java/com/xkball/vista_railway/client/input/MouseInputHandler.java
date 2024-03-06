package com.xkball.vista_railway.client.input;

import com.xkball.vista_railway.api.item.IMouseWheelRespond;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.packets.MouseWheelInputToServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MouseInputHandler {
    
    @SubscribeEvent
    public static void onKeyPressed(MouseEvent event) {
        var dWheel = event.getDwheel();
        if(dWheel == 0){
            return;
        }
        var itemInHand = Minecraft.getMinecraft().player.getHeldItemMainhand();
        if(itemInHand.getItem() instanceof IMouseWheelRespond respond && respond.canRespondTo(itemInHand,dWheel)){
            GCNetworkManager.INSTANCE.sendPacketToServer(new MouseWheelInputToServerPacket(dWheel,itemInHand));
            event.setCanceled(respond.cancelEvent(itemInHand, dWheel));
        }
    }
}
