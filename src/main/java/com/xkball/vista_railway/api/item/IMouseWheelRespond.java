package com.xkball.vista_railway.api.item;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMouseWheelRespond {
    @SideOnly(Side.CLIENT)
    boolean canRespondTo(ItemStack itemStack ,int dWheel);
    
    @SideOnly(Side.CLIENT)
    boolean cancelEvent(ItemStack itemStack ,int dWheel);
    
    void respondToMouseWheel(EntityPlayerMP playerMP,ItemStack itemStack , int dWheel);
    
}
