package com.xkball.vista_railway.client.gui.overlay;

import com.xkball.vista_railway.utils.ColorUtils;
import com.xkball.vista_railway.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OverlayRow {
    public static final int TEXT_COLOR = ColorUtils.getColor(202,218,252,255);
    private ConfigurableOverlay overlay;
    private final List<OverlayColumn> columnList = new ArrayList<>();
    private final String tagName;
    private String renderString = "";
    private final boolean selectable;
    private int currentSelected = 0;
    private int renderID = -1;
    private int columnW = -1;

    
    public OverlayRow(String tagName, boolean selectable) {
        this.tagName = tagName;
        this.selectable = selectable;
    }
    
    public OverlayRow addAll(List<Pair<String,Integer>> list){
        list.sort(Comparator.comparingInt(Pair::getRight));
        for (var pair : list) {
            this.addOverlayColumn(new OverlayColumn()
                    .setRenderString(pair.getLeft()));
        }
        return this;
    }
    
    public OverlayRow addOverlayColumn(OverlayColumn buttonColumn){
        columnList.add(buttonColumn);
        buttonColumn.setRenderID(columnList.size());
        buttonColumn.setRow(this);
        return this;
    }
    
    public void readFromNBT(NBTTagCompound tag){
        if(tag.hasKey("current_selected")){
            currentSelected = tag.getInteger("current_selected");
            if(currentSelected>columnList.size()) currentSelected=0;
        }
        for (OverlayColumn overlayColumn : columnList) {
            var tagName = overlayColumn.getTagName();
            if(!tagName.isEmpty() && tag.hasKey(tagName)) overlayColumn.readFromNBT(tag.getCompoundTag(tagName));
        }
    }
    
    public NBTTagCompound saveToNBT(NBTTagCompound tag){
        tag.setInteger("current_selected",currentSelected);
        for (OverlayColumn overlayColumn : columnList){
            if(!overlayColumn.getTagName().isEmpty()){
                tag.setTag(overlayColumn.getTagName(), overlayColumn.saveToNBT());
            }
        }
        return tag;
    }
    
    @SideOnly(Side.CLIENT)
    public void render(int x,int y,int minW,int minY,int selectedID){
        if(columnW<0){
            for(var column : columnList){
                columnW = Math.max(40,Minecraft.getMinecraft().fontRenderer.getStringWidth(RenderUtils.getRenderString(column.getRenderString()))+4);
            }
        }
        var l0 = Math.max(Minecraft.getMinecraft().fontRenderer.getStringWidth(RenderUtils.getRenderString(renderString))+8,minW);
        RenderUtils.drawItemOverlayBG(x,y,l0,minY,61,96,172,200);
        RenderUtils.drawCenteredL10nString(renderString,x,y,l0,minY,TEXT_COLOR,true);
        if(selectedID == renderID){
            RenderUtils.renderBoundary(x-1,y-1,l0+2,minY+2,61,96,172,255);
        }
        var rx = x+l0;
        for(var column : columnList){
            rx+=column.render(rx,y,Math.max(columnW,column.getW_()),minY,currentSelected);
        }
    }
    
    public void respondToMouseWheel(EntityPlayerMP playerMP, ItemStack itemStack, int dWheel){
        if(dWheel<0) {
            currentSelected++;
        }
        else {
            currentSelected+=columnList.size();
        }
        currentSelected%=(columnList.size()+1);
    }
    
    public boolean selectable(){
        return selectable;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    public int columnSize(){
        return columnList.size();
    }
    
    public String getRenderString() {
        return renderString;
    }
    
    public OverlayRow setRenderString(String renderString) {
        this.renderString = renderString;
        return this;
    }
    
    public int getRenderID() {
        return renderID;
    }
    
    public void setRenderID(int renderID) {
        this.renderID = renderID;
    }
    
    public List<OverlayColumn> getColumnList() {
        return columnList;
    }
    
    public int getCurrentSelected() {
        return currentSelected;
    }
    
    public ConfigurableOverlay getOverlay() {
        return overlay;
    }
    
    public void setOverlay(ConfigurableOverlay overlay) {
        this.overlay = overlay;
    }
    
}
