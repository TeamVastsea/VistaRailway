package com.xkball.vista_railway.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public class ClientMessageManager {
    
    public static final ClientMessageManager INSTANCE = new ClientMessageManager();
    
    private final Queue<ITextComponent> messageQueue = new LinkedList<>();
    
    private ClientMessageManager(){
    
    }
    public void update(){
        while (Minecraft.getMinecraft().world != null && !messageQueue.isEmpty()){
            Minecraft.getMinecraft().player.sendMessage(messageQueue.poll());
        }
    }
    
    public void info(String str){
        messageQueue.add(new TextComponentString(str));
    }
    
    public void debug(String str){
        if(FMLLaunchHandler.isDeobfuscatedEnvironment()){
            messageQueue.add(new TextComponentString(str).setStyle(new Style().setColor(TextFormatting.YELLOW)));
        }
    }
    
    public void warn(String str){
        messageQueue.add(new TextComponentString(str).setStyle(new Style().setColor(TextFormatting.RED)));
    }
}
