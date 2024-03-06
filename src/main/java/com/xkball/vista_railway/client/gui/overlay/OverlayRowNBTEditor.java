package com.xkball.vista_railway.client.gui.overlay;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.util.TriConsumer;

public class OverlayRowNBTEditor extends OverlayRow {
    
    private NBTTagCompound tag = new NBTTagCompound();
    private TriConsumer<OverlayRow,NBTTagCompound,Integer> mouseWheelRespond = null;
    public OverlayRowNBTEditor(String tagName) {
        super(tagName, true);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.tag = tag.getCompoundTag("tag");
    }
    
    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound tag) {
        tag.setTag("tag",this.tag);
        return super.saveToNBT(tag);
    }
    
    @Override
    public void respondToMouseWheel(EntityPlayerMP playerMP, ItemStack itemStack, int dWheel) {
        if (mouseWheelRespond==null) return;
        mouseWheelRespond.accept(this,tag,dWheel);
    }
    
    public NBTTagCompound getTag() {
        return tag;
    }
    
    public void setTag(NBTTagCompound tag) {
        this.tag = tag;
    }
    
    public OverlayRowNBTEditor setMouseWheelRespond(TriConsumer<OverlayRow,NBTTagCompound,Integer> mouseWheelRespond) {
        this.mouseWheelRespond = mouseWheelRespond;
        return this;
    }
}
