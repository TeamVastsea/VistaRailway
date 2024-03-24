package com.xkball.vista_railway.client;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.mixin.VRMixinBlockModelRendererAccess;
import com.xkball.vista_railway.registration.VRBlocks;
import com.xkball.vista_railway.utils.FixedLightBlockAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class VRModelManager {
    private static final Map<String, IBakedModel> models = new HashMap<>();
    private static final Map<String, BufferBuilder> buffers = new HashMap<>();
    
    public static VRModelManager INSTANCE = new VRModelManager();
    
    public static BlockModelRenderer MODEL_RENDERER = null;
    
    private VRModelManager(){
    
    }
    
    public IBakedModel getModel(String name){
        var result = models.get(name);
        if(result != null) return result;
        try {
            var rawModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(VistaRailway.MOD_ID,"block/"+name+".obj"));
            var model = rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL,
                    (r) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(r.toString()));
            models.put(name,model);
            return model;
        }catch (Exception e){
            if("empty".equals(name)){
                throw new RuntimeException("it shouldn't fail to load,is the mod file broken?");
            }
            LogManager.getLogger().error("happened error when loading model " +VistaRailway.MOD_ID+"block/"+name+".obj" );
            LogManager.getLogger().error(e);
        }
        return getModel("empty");
    }
    
    public BufferBuilder getBuffer(String name){
        var result = buffers.get(name);
        if (result != null) return result;
        if(MODEL_RENDERER == null){
            var rawRender = (VRMixinBlockModelRendererAccess)Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer();
            MODEL_RENDERER = new VRBlockModelRenderer(rawRender.getBlockColors());
            //MODEL_RENDERER = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer();
        }
        var buffer = new BufferBuilder(131072);
        buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
        var world = Minecraft.getMinecraft().world;
        MODEL_RENDERER.renderModel(new FixedLightBlockAccess(world,15), getModel(name),
                        VRBlocks.POLE_BLOCK.getDefaultState(), BlockPos.ORIGIN,buffer,false);
        buffer.finishDrawing();
        buffers.put(name,buffer);
        return buffer;
    }
}
