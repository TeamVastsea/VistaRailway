package com.xkball.vista_railway.client.gui;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.registration.VRBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class VRModelManager {
    private static final Map<String, IBakedModel> models = new HashMap<>();
    private static final Map<String, BufferBuilder> buffers = new HashMap<>();
    
    public static VRModelManager INSTANCE = new VRModelManager();
    
    private VRModelManager(){
    
    }
    
    public IBakedModel getModel(String name){
        var result = models.get(name);
        if(result != null) return result;
        var rawModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(VistaRailway.MOD_ID,"block/"+name+".obj"));
        var model = rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL,
                (r) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(r.toString()));
        models.put(name,model);
        return model;
    }
    
    public BufferBuilder getBuffer(String name){
        var result = buffers.get(name);
        if (result != null) return result;
        var buffer = new BufferBuilder(131072);
        buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
        var world = Minecraft.getMinecraft().world;
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
                .renderModel(world, getModel(name),
                        VRBlocks.POLE_BLOCK.getDefaultState(), BlockPos.ORIGIN,buffer,false);
        buffer.finishDrawing();
        buffers.put(name,buffer);
        return buffer;
    }
}
