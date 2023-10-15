package com.xkball.vista_railway.client.render.tesr;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.common.te.CatenaryTE;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CatenaryRender extends TileEntitySpecialRenderer<CatenaryTE> {
    
    public static final ResourceLocation BlackTexture = new ResourceLocation(VistaRailway.MODID,"textures/block/black.png");
    @Override
    public void render(CatenaryTE te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(!te.render || te.toRender == null) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        final var buffer = Tessellator.getInstance().getBuffer();
       // GlStateManager.matrixMode(GL11.GL_COLOR);
       // GlStateManager.enableRescaleNormal();
       // GlStateManager.color(0,0,0);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(BlackTexture);
        GlStateManager.translate(x,y,z);
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(),-te.getPos().getZ());
        te.toRender.render(buffer,255,255,255,255);
        Tessellator.getInstance().draw();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean isGlobalRenderer(CatenaryTE te) {
        return true;
    }
    
    
}
