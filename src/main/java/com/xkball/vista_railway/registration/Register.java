package com.xkball.vista_railway.registration;

import com.xkball.vista_railway.common.block.PoleBlock;
import com.xkball.vista_railway.common.te.PoleTE;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber
public class Register {
    
    @SubscribeEvent
    public static void onBlockReg(RegistryEvent.Register<Block> event){
        var registry = event.getRegistry();
        registry.register(VRBlocks.POLE_BLOCK);
        GameRegistry.registerTileEntity(PoleTE.class, PoleBlock.ID);
    }
    
    @SubscribeEvent
    public static void onItemReg(RegistryEvent.Register<Item> event){
        var registry = event.getRegistry();
        registry.register(VRItems.POLE_BLOCK_ITEM);
        registry.register(VRItems.BINDER_ITEM);
        registry.register(VRItems.TRANSLATOR_ITEM);
    }
    
    @SubscribeEvent
    public static void onModelReg(ModelRegistryEvent event) {
        bindDefaultModel(VRItems.POLE_BLOCK_ITEM);
        bindDefaultModel(VRItems.BINDER_ITEM);
        bindDefaultModel(VRItems.TRANSLATOR_ITEM);
    }
    
    public static void bindDefaultModel(Item item){
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        
    }
    
}
