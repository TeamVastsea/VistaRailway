package com.xkball.vista_railway.mixin;

import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ForgeBlockModelRenderer.class)
public interface VRMixinForgeBlockModelRendererAccess {
    
    @Accessor(remap = false)
    ThreadLocal<VertexBufferConsumer> getConsumerFlat();
    
    @Accessor(remap = false)
    ThreadLocal<VertexBufferConsumer> getConsumerSmooth();
    
    @Accessor(remap = false)
    ThreadLocal<VertexLighterFlat> getLighterFlat();
    
    @Accessor(remap = false)
    ThreadLocal<VertexLighterSmoothAo> getLighterSmooth();
}
