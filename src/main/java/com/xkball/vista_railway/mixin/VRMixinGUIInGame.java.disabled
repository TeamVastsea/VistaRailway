package com.xkball.vista_railway.mixin;

import com.xkball.vista_railway.api.item.IOverlayProviderItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class VRMixinGUIInGame {
    
    @Inject(method = "renderGameOverlay",at = @At("RETURN"),remap = false)
    public void onRender(float partialTicks, CallbackInfo ci){
        var player = Minecraft.getMinecraft().player;
        if (player != null) {
            var itemStack = player.getHeldItemMainhand();
            if (itemStack.getItem() instanceof IOverlayProviderItem iOverlayProviderItem) {
                iOverlayProviderItem.renderOverlay(Minecraft.getMinecraft(),itemStack,partialTicks);
            }
        }
    }
}
