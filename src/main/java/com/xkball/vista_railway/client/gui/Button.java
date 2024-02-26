package com.xkball.vista_railway.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Button extends Renderable{
    
    boolean enabled();
    
    default void mouseClicked(int mouseX, int mouseY, int mouseButton){
        playPressSound();
    }
    
    default void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    
    }
    
    default void mouseReleased(int mouseX, int mouseY, int state){
    
    }
    
    default void playPressSound()
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
