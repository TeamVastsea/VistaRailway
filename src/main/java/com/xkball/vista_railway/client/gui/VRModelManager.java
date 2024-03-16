package com.xkball.vista_railway.client.gui;

import com.xkball.vista_railway.VistaRailway;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.HashMap;
import java.util.Map;

public class VRModelManager {
    private static final Map<String, IBakedModel> models = new HashMap<>();
    
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
}
