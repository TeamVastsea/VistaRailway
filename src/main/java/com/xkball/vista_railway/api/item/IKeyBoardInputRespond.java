package com.xkball.vista_railway.api.item;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IKeyBoardInputRespond {

    boolean canRespondTo(ItemStack itemStack,KeyBinding keyBinding);
    
    void onKeyPressed(ItemStack itemStack,int key);
}
