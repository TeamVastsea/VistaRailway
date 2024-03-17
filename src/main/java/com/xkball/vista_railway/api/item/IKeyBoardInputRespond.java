package com.xkball.vista_railway.api.item;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public interface IKeyBoardInputRespond {
    @SideOnly(Side.CLIENT)
    boolean canRespondTo(ItemStack itemStack,KeyBinding keyBinding);
    
    void onKeyPressed(ItemStack itemStack,int key);
}
