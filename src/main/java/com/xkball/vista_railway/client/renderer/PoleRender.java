package com.xkball.vista_railway.client.renderer;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.api.item.IPoleRenderable;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class PoleRender extends TileEntitySpecialRenderer<PoleTE> {
    public static final ResourceLocation OBJ_MODEL = new ResourceLocation(VistaRailway.MOD_ID,"block/2.obj");
    public static final ResourceLocation WHITE = new ResourceLocation(VistaRailway.MOD_ID,"textures/block/white.png");
    IBakedModel model = null;
    @Override
    public void render(@Nullable PoleTE te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(model == null){
        //var modelManager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
        var rawModel = ModelLoaderRegistry.getModelOrMissing(OBJ_MODEL);
        model = rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.BLOCK,
                (r) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(r.toString()));
        }
//        var pos = te.getPos();
        var player = Minecraft.getMinecraft().player;
        if(te != null){
        
        }
        if(player.getHeldItemMainhand().getItem() instanceof IPoleRenderable || player.getHeldItemOffhand().getItem() instanceof IPoleRenderable){
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.disableDepth();
         //   GlStateManager.clearColor(0,0,0,0);
            GlStateManager.disableLighting();
            GlStateManager.translate(x,y,z);
            Minecraft.getMinecraft().renderEngine.bindTexture(WHITE);
            var buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_TEX_COLOR);
            var size = 1;
            RenderUtils.renderRGBCube(buffer,size);
            Tessellator.getInstance().draw();
            if(te != null){
                var offset = te.data.offset;
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
