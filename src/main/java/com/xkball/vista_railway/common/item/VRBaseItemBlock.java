package com.xkball.vista_railway.common.item;

import com.xkball.vista_railway.registration.Register;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class VRBaseItemBlock extends ItemBlock {
    public VRBaseItemBlock(Block block) {
        super(block);
        var id = block.getRegistryName();
        if(id != null){
            this.setRegistryName(id);
        }
        this.setCreativeTab(Register.VR_TAB);
    }
}
