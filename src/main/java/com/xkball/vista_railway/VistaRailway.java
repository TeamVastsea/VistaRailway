package com.xkball.vista_railway;


import com.xkball.vista_railway.client.input.KeyBoardInputHandler;
import com.xkball.vista_railway.client.renderer.PoleRender;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.network.GCNetworkManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@Mod(modid = VistaRailway.MOD_ID, name = VistaRailway.NAME, version = VistaRailway.VERSION)
public class VistaRailway {
    
    public static final String MOD_ID = "vista_railway";
    
    public static final String NAME = "Vista Railway";
    public static final String VERSION = "0.1.0";
    
    public static final File configDir = Loader.instance().getConfigDir();
    
    public static int clientTick = 0;
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GCNetworkManager.init();
        KeyBoardInputHandler.init();
    }
    
    @Mod.EventBusSubscriber
    public static class ModEventHandler{
        
        
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void modelRegistryEvent(ModelRegistryEvent event) {
            OBJLoader.INSTANCE.addDomain(MOD_ID);
            ClientRegistry.bindTileEntitySpecialRenderer(PoleTE.class,new PoleRender());
        }
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onClientTick(TickEvent.ClientTickEvent event){
            if(event.phase == TickEvent.Phase.START) clientTick++;
        }
        
        
    }
    @Mod.EventHandler
    public static void onServerStart(FMLServerStartedEvent event){
        CatenaryDataManager.INSTANCE.loadFromDirectory(new File(configDir.getPath()+"/vista_railway_catenary_style.json").toPath());
    }
 
}
