package com.xkball.vista_railway.client.input;

import com.xkball.vista_railway.api.item.IKeyBoardInputRespond;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.packets.KeyBoardInputToServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class KeyBoardInputHandler {
    public static final KeyBinding ITEM_SWITCH_MODE = new KeyBinding("vista_railway.hot_key.item_switch_mode",
            KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_TAB, "key.category.vista_railway");

    public static void init() {
        ClientRegistry.registerKeyBinding(ITEM_SWITCH_MODE);
    }
    
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (ITEM_SWITCH_MODE.isPressed()) {
            var itemInhand = Minecraft.getMinecraft().player.getHeldItemMainhand();
            if (itemInhand.getItem() instanceof IKeyBoardInputRespond respond && respond.canRespondTo(itemInhand,ITEM_SWITCH_MODE)) {
                GCNetworkManager.INSTANCE.sendPacketToServer(new KeyBoardInputToServerPacket(ITEM_SWITCH_MODE.getKeyCode(),Minecraft.getMinecraft().player.getHeldItemMainhand()));
            }
        }
    }
}
