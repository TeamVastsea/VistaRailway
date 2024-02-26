package com.xkball.vista_railway.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class VRBaseItemBlock extends ItemBlock {
    public VRBaseItemBlock(Block block) {
        super(block);
        var id = block.getRegistryName();
        if(id != null){
            this.setRegistryName(id);
        }
    }
}
