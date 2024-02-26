package com.xkball.vista_railway.client.gui;

import com.xkball.vista_railway.client.gui.screen.VRBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Renderable {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    void render(Minecraft minecraft, int mouseX, int mouseY, float partialTicks);
    
    default boolean isMouseIn(int mouseX, int mouseY){
        return mouseX>getX()&&mouseX<getX()+getWidth()&&mouseY>getY()&&mouseY<getY()+getHeight();
    }
    
    default boolean visible(){
        return true;
    }
    
    default void onAdd(VRBaseScreen screen){
    
    }
}
