package com.xkball.vista_railway.api.item;

import com.xkball.vista_railway.client.gui.overlay.ConfigurableOverlay;
import com.xkball.vista_railway.utils.AssertUtils;
import com.xkball.vista_railway.utils.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public interface IOverlayProviderItem {
    
    ConfigurableOverlay getOverlay();
    @SideOnly(Side.CLIENT)
    void renderOverlay(Minecraft minecraft, ItemStack itemStack, float partialTick);
    
    default void loadToOverlay(ItemStack itemStack){
        var tag = NBTUtils.getOrCreateTag(itemStack);
        getOverlay().readFromNBT(tag);
    }
    
    default void saveFromOverlay(ItemStack itemStack){
        var tag = NBTUtils.getOrCreateTag(itemStack);
        itemStack.setTagCompound(getOverlay().saveToNBT(tag));
    }
    
}
