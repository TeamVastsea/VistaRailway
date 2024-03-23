package com.xkball.vista_railway.mixin;

import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.color.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockModelRenderer.class)
public interface VRMixinBlockModelRendererAccess {
    
    @Accessor
    BlockColors getBlockColors();
}
