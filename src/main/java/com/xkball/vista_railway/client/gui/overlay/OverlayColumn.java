package com.xkball.vista_railway.client.gui.overlay;

import com.xkball.vista_railway.utils.AssertUtils;
import com.xkball.vista_railway.utils.RenderUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class OverlayColumn {
    private OverlayRow row;
    private String renderString;
    private String tagName = "";
    @Nullable
    private BiFunction<OverlayColumn,NBTTagCompound,String> renderStringFromNBT = null;
    private int renderID = -1;
    private int w_ = 0;
    @Nullable
    public NBTTagCompound tempTag = null;
    
    public OverlayColumn(){
    
    }
    
    public OverlayColumn setTagName(String tagName, BiFunction<OverlayColumn,NBTTagCompound, String> renderStringFromNBT){
        this.tagName = tagName;
        this.renderStringFromNBT = renderStringFromNBT;
        return this;
    }
    
    public void readFromNBT(NBTTagCompound tag){
        this.tempTag = tag;
        if (renderStringFromNBT != null) {
            renderString = renderStringFromNBT.apply(this,tag);
        }
    }
    
    public NBTTagCompound saveToNBT(){
        return AssertUtils.requireNonNullElseGet(tempTag,NBTTagCompound::new);
    }
    
    @SideOnly(Side.CLIENT)
    public int render(int x,int y,int w,int h,int selectedID){
        RenderUtils.drawItemOverlayBG(x,y,w,h,60,172,255,200);
        RenderUtils.drawCenteredL10nString(renderString,x,y,w,h, OverlayRow.TEXT_COLOR,selectedID == renderID);
        if(selectedID == renderID){
            RenderUtils.renderBoundary(x,y,w,h,61,96,172,255);
        }
        return w;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    public String getRenderString() {
        return renderString;
    }
    
    public OverlayColumn setRenderString(String renderString) {
        this.renderString = renderString;
        return this;
    }
    
    public int getRenderID() {
        return renderID;
    }
    
    public void setRenderID(int renderID) {
        this.renderID = renderID;
    }
    
    public int getW_() {
        return w_;
    }
    
    public OverlayColumn setW(int w_) {
        this.w_ = w_;
        return this;
    }
    
    public OverlayRow getRow() {
        return row;
    }
    
    public void setRow(OverlayRow row) {
        this.row = row;
    }
}
