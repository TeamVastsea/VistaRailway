package com.xkball.vista_railway;

import com.xkball.vista_railway.client.render.tesr.CatenaryRender;
import com.xkball.vista_railway.common.te.CatenaryTE;
import com.xkball.vista_railway.network.network.GCNetworkManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = VistaRailway.MODID, name = VistaRailway.NAME, version = VistaRailway.VERSION)
public class VistaRailway
{
    public static final String MODID = "xkball_railway";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String NAME = "xkball's Railway";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        //logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        GCNetworkManager.init();
    }
    
    @Mod.EventBusSubscriber
    public static class ModEventHandler{
        
        public static int clientTick = 0;
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void modelRegistryEvent(ModelRegistryEvent event) {
            OBJLoader.INSTANCE.addDomain(MODID);
            ClientRegistry.bindTileEntitySpecialRenderer(CatenaryTE.class,new CatenaryRender());
        }
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onClientTick(TickEvent.ClientTickEvent event){
            if(event.phase == TickEvent.Phase.START) clientTick++;
        }
    }
}
