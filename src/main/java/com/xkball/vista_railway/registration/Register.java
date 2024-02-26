package com.xkball.vista_railway.registration;

import com.xkball.vista_railway.common.block.PoleBlock;
import com.xkball.vista_railway.common.te.PoleTE;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class Register {
    
    @SubscribeEvent
    public static void onBlockReg(RegistryEvent.Register<Block> event){
        event.getRegistry().register(VRBlocks.POLE_BLOCK);
        GameRegistry.registerTileEntity(PoleTE.class, PoleBlock.ID);
    }
    
    @SubscribeEvent
    public static void onItemReg(RegistryEvent.Register<Item> event){
        event.getRegistry().register(VRItems.POLE_BLOCK_ITEM);
    }
    
}
