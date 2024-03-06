package com.xkball.vista_railway.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class VRBaseItem extends Item {
    
    public VRBaseItem(ResourceLocation id) {
        if(id != null){
            this.setRegistryName(id);
        }
    }
    
}
