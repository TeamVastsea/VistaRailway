package com.xkball.vista_railway.init;

import com.xkball.vista_railway.common.block.CatenaryBlock;
import com.xkball.vista_railway.common.item.TestItem;
import com.xkball.vista_railway.common.te.CatenaryTE;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class Register {
    
    public static CatenaryBlock catenaryBlock;
    public static ItemBlock catenaryBlockItem;
    
    public static TestItem testItem2;
    public static TestItem testItem3;
    public static TestItem testItem4;
    
    @SubscribeEvent
    public static void onBlockReg(RegistryEvent.Register<Block> event){
        catenaryBlock = new CatenaryBlock();
        event.getRegistry().register(catenaryBlock );
        GameRegistry.registerTileEntity(CatenaryTE.class,CatenaryBlock.ID);
    }
    
    @SubscribeEvent
    public static void onItemReg(RegistryEvent.Register<Item> event){
        catenaryBlockItem = new ItemBlock(catenaryBlock);
        catenaryBlockItem.setRegistryName(CatenaryBlock.ID);
        event.getRegistry().register(catenaryBlockItem);
        testItem2 = new TestItem(2);
        testItem3 = new TestItem(3);
        testItem4 = new TestItem(4);
        event.getRegistry().registerAll(testItem2,testItem3,testItem4);
    }
}
