package com.xkball.vista_railway.client.render;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.common.te.PoleTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PoleRender extends TileEntitySpecialRenderer<PoleTE> {
    public static final ResourceLocation OBJ_MODEL = new ResourceLocation(VistaRailway.MOD_ID,"block/2.obj");
    AxisAlignedBB aabb = new AxisAlignedBB(BlockPos.ORIGIN).shrink(0.1);
    IBakedModel model = null;
    @Override
    public void render(PoleTE te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(model == null){
        //var modelManager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
        var rawModel = ModelLoaderRegistry.getModelOrMissing(OBJ_MODEL);
        model = rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.BLOCK,
                (r) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(r.toString()));
        }
        var pos = te.getPos();
        var player = Minecraft.getMinecraft().player;
        var l = new Vector3f((float) (pos.getX()-TileEntityRendererDispatcher.staticPlayerX),
                (float) (pos.getY()-TileEntityRendererDispatcher.staticPlayerY),
                (float) (pos.getZ()-TileEntityRendererDispatcher.staticPlayerZ)).length();
        
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.glLineWidth(16/l + 4.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.translate(x,y,z);
        
        var blockRenderDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        //blockRenderDispatcher.getBlockModelRenderer().renderModelBrightnessColor(model,1F,1F,1F,1F);
       
        
        RenderGlobal.drawSelectionBoundingBox(aabb,0f,0f,0f,1f);
        GlStateManager.glLineWidth(1);
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
    
    @Override
    public boolean isGlobalRenderer(com.xkball.vista_railway.common.te.PoleTE te) {
        return super.isGlobalRenderer(te);
    }
}
