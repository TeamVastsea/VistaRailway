package com.xkball.vista_railway.common.item;

import com.xkball.vista_railway.api.item.IPoleRenderable;
import com.xkball.vista_railway.registration.VRBlocks;
import net.minecraft.block.Block;

public class PoleItemBlock extends VRBaseItemBlock implements IPoleRenderable {
    public PoleItemBlock() {
        super(VRBlocks.POLE_BLOCK);
    }
}
