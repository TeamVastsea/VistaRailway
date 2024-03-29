package com.xkball.vista_railway.client.renderer;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.api.item.IPoleRenderable;
import com.xkball.vista_railway.client.global.ConsistenceVertexBufferUploader;
import com.xkball.vista_railway.client.VRModelManager;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.data.ModelData;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class PoleRender extends TileEntitySpecialRenderer<PoleTE> {
    public static final ResourceLocation WHITE = new ResourceLocation(VistaRailway.MOD_ID,"textures/block/white.png");
    public static final WorldVertexBufferUploader bufferUploader = new ConsistenceVertexBufferUploader();
    
    @Override
    public void render(@Nullable PoleTE te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        var player = Minecraft.getMinecraft().player;
        if(te != null){
            if(te.renderData.needUpdate) te.renderData.readFromTE(te);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x,y,z);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(WHITE);
            if(te.hasStyle()){
                var style = CatenaryDataManager.INSTANCE.get(te.data.styleID);
                for (ModelData modelData : style.rendering()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(te.data.offset.x,te.data.offset.y,te.data.offset.z);
                    GlStateManager.scale(modelData.scale(),modelData.scale(),modelData.scale());
                    GlStateManager.rotate(modelData.rotation().toQuaternion());
                    GlStateManager.rotate(te.data.yRotation,0,1,0);
                    GlStateManager.translate(modelData.offset().x,modelData.offset().y,modelData.offset().z);
                    RenderUtils.bindOBJTexture(modelData.modelPath());
                    var buffer = VRModelManager.INSTANCE.getBuffer(modelData.modelPath());
                    bufferUploader.draw(buffer);
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.translate(-te.getPos().getX(),-te.getPos().getY(),-te.getPos().getZ());
            GlStateManager.disableCull();
            te.renderData.render(100,100,100,255);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
            //GlStateManager.enableDepth();
        }
        if(player.getHeldItemMainhand().getItem() instanceof IPoleRenderable || player.getHeldItemOffhand().getItem() instanceof IPoleRenderable){
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.translate(x,y,z);
            Minecraft.getMinecraft().renderEngine.bindTexture(WHITE);
            var buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_TEX_COLOR);
            var size = 1;
            RenderUtils.renderRGBCube(buffer,size);
            Tessellator.getInstance().draw();
            if(te != null){
                var offset = te.data.offset.toVec3f();
                if (offset.lengthSquared()>0.2) {
                    buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_TEX_COLOR);
                    GlStateManager.translate(offset.x,offset.y,offset.z);
                    RenderUtils.renderRGBCube(buffer,0.5);
                    Tessellator.getInstance().draw();
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
            GlStateManager.enableDepth();
        }
    
    }
    
    @Override
    public boolean isGlobalRenderer(com.xkball.vista_railway.common.te.PoleTE te) {
        return true;
    }
}
