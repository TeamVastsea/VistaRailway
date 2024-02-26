package com.xkball.vista_railway.client.gui.component;

import com.xkball.vista_railway.client.gui.Button;
import com.xkball.vista_railway.client.gui.Renderable;
import com.xkball.vista_railway.common.data.CatenaryStyleData;
import com.xkball.vista_railway.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GUICatenaryStyleLabel extends Gui implements Button {
    public static final int boundingLineColor = ColorUtils.getColor(200,200,200,200);
    public static final int mouseInColor = ColorUtils.getColor(150,150,150,150);
    private static final int selectingColor = ColorUtils.getColor(170,170,170,150);
    public boolean visible = true;
    public int w;
    public int h;
    public int x;
    public int y;
    public boolean selected = false;
    public boolean selecting = false;
    
    protected String name;
    protected int nodeCount;
    public final int id;
    
    public GUICatenaryStyleLabel(CatenaryStyleData catenaryStyleData,int x,int y,int screenWidth,int screenHigh){
        this.w = screenWidth/5-1;
        this.h = Math.max(screenHigh/20,30);
        this.x = x;
        this.y = y;
        this.name = catenaryStyleData.name();
        this.nodeCount = catenaryStyleData.nodeCount();
        this.id = catenaryStyleData.id();
        var key = "vista_railway.catenary.name."+name;
        if(I18n.hasKey(key)){
            this.name = I18n.format(key);
        }
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0){
            selecting = !selecting;
            Button.super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        
    }
    
    @Override
    public void render(Minecraft minecraft,int mouseX,int mouseY,float partialTicks){
        drawHorizontalLine(x,x+w,y,boundingLineColor);
        drawHorizontalLine(x,x+w,y+h,boundingLineColor);
        drawVerticalLine(x,y,y+h,boundingLineColor);
        drawVerticalLine(x+1,y,y+h,boundingLineColor);
        drawVerticalLine(x+w,y,y+h,boundingLineColor);
        if(isMouseIn(mouseX,mouseY)&&!selected&&!selecting){
            drawRect(x+1,y+1,x+w,y+h,mouseInColor);
        }
        if(selecting){
            drawRect(x+1,y+1,x+w,y+h,selectingColor);
        }
        if(selected){
            var str = TextFormatting.GREEN+I18n.format("vista_railway.gui.selected");
            var l = minecraft.fontRenderer.getStringWidth(str);
            drawString(minecraft.fontRenderer, str,x+w-l-4,y+5,-1);
        }
        drawString(minecraft.fontRenderer,I18n.format("vista_railway.catenary.style",name),x+4,y+5,ColorUtils.white);
        drawString(minecraft.fontRenderer,I18n.format("vista_railway.catenary.node_count",nodeCount),x+4,y+h-12,ColorUtils.white);
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }
    
    @Override
    public int getWidth() {
        return w;
    }
    
    @Override
    public int getHeight() {
        return h;
    }
    
    @Override
    public boolean visible() {
        return visible;
    }
    
    @Override
    public boolean enabled() {
        return !selected;
    }
}
