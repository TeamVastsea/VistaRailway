package com.xkball.vista_railway.utils;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.client.ClientMessageManager;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.xkball.vista_railway.utils.ColorUtils.ClientColorUtils.*;

@SideOnly(Side.CLIENT)
public class RenderUtils {
    
    public static final ResourceLocation ITEM_OVERLAY_BG = new ResourceLocation(VistaRailway.MOD_ID,"textures/item_overlay_bg.png");
    
    public static final Object2ObjectMap<String,Pair<Boolean,ResourceLocation>> hasTexture = new Object2ObjectLinkedOpenHashMap<>();
    public static void renderRGBCube(BufferBuilder buffer, double size){
        var r = clientCurrentRed();
        var g = clientCurrentGreen();
        var b = clientCurrentBlue();
        buffer.pos(0,0,0).tex(0,0).color(r,g,b,255).endVertex();
        buffer.pos(0,size,0).tex(0,1).color(b,r,g,255).endVertex();
        buffer.pos(size,size,0).tex(1,1).color(r,g,b,255).endVertex();
        buffer.pos(size,0,0).tex(1,0).color(b,r,g,255).endVertex();
        
        buffer.pos(0,0,size).tex(0,0).color(r,g,b,255).endVertex();
        buffer.pos(0,size,size).tex(0,1).color(b,r,g,255).endVertex();
        buffer.pos(size,size,size).tex(1,1).color(r,g,b,255).endVertex();
        buffer.pos(size,0,size).tex(1,0).color(b,r,g,255).endVertex();
        
        buffer.pos(0,0,0).tex(0,0).color(r,g,b,255).endVertex();
        buffer.pos(0,0,size).tex(0,1).color(b,r,g,255).endVertex();
        buffer.pos(0,size,size).tex(1,1).color(r,g,b,255).endVertex();
        buffer.pos(0,size,0).tex(1,0).color(b,r,g,255).endVertex();
        
        buffer.pos(size,0,0).tex(0,0).color(r,g,b,255).endVertex();
        buffer.pos(size,0,size).tex(0,1).color(b,r,g,255).endVertex();
        buffer.pos(size,size,size).tex(1,1).color(r,g,b,255).endVertex();
        buffer.pos(size,size,0).tex(1,0).color(b,r,g,255).endVertex();
        
        buffer.pos(0,0,0).tex(0,0).color(r,g,b,255).endVertex();
        buffer.pos(0,0,size).tex(0,1).color(b,r,g,255).endVertex();
        buffer.pos(size,0,size).tex(1,1).color(r,g,b,255).endVertex();
        buffer.pos(size,0,0).tex(1,0).color(b,r,g,255).endVertex();
        
        buffer.pos(0,size,0).tex(0,0).color(r,g,b,255).endVertex();
        buffer.pos(0,size,size).tex(0,1).color(b,r,g,255).endVertex();
        buffer.pos(size,size,size).tex(1,1).color(r,g,b,255).endVertex();
        buffer.pos(size,size,0).tex(1,0).color(b,r,g,255).endVertex();
    }
    
    public static void drawItemOverlayBG(int x,int y,int w,int h,int r,int g,int b,int a){
        Minecraft.getMinecraft().renderEngine.bindTexture(ITEM_OVERLAY_BG);
        GlStateManager.enableBlend();
        var buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        w+=x;
        h+=y;
        var xDraw = x;
        var xTexL = x%32;
        var xTexH = Math.min(32,w-xDraw);
        //看乐了
        for(;xDraw<w;xDraw+=xDraw%32==0?xTexH:32-xDraw%32,xTexL = 0,xTexH = Math.min(32,w-xDraw)){
            var yDraw = y;
            var yTexL = y%32;
            var yTexH = Math.min(32,h-yDraw);
            for(;yDraw<h;yDraw+=yDraw%32==0?yTexH:32-yDraw%32,yTexL = 0,yTexH = Math.min(32,h-yDraw)){
                buffer.pos(xDraw,yDraw,0).tex(xTexL/32D,yTexL/32D).color(r,g,b,a).endVertex();
                buffer.pos(xDraw,yDraw+(yTexH-yTexL),0).tex(xTexL/32D,yTexH/32D).color(r,g,b,a).endVertex();
                buffer.pos(xDraw+(xTexH-xTexL),yDraw+(yTexH-yTexL),0).tex(xTexH/32D,yTexH/32D).color(r,g,b,a).endVertex();
                buffer.pos(xDraw+(xTexH-xTexL),yDraw,0).tex(xTexH/32D,yTexL/32D).color(r,g,b,a).endVertex();
            }
        }
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
    }
    
    public static void renderBoundary(int x,int y,int w,int h,int r,int g,int b,int a){
        var color = ColorUtils.getColor(r,g,b,a);
        Gui.drawRect(x,y,x+w,y+1,color);
        Gui.drawRect(x,y+h-1,x+w,y+h,color);
        Gui.drawRect(x,y,x+1,y+h,color);
        Gui.drawRect(x+w-1,y,x+w,y+h,color);
        GlStateManager.color(0,0,0,0);
    }
    
    public static void drawCenteredString(String str,int x,int y,int w,int h,int color,boolean dropShadow){
        var fontRender = Minecraft.getMinecraft().fontRenderer;
        var l = fontRender.getStringWidth(str);
        if(dropShadow){
            fontRender.drawString(str,x+(w-l)/2f+1,y+h/2f-4+1,ColorUtils.getColor(ColorUtils.black,150),false);
        }
        fontRender.drawString(str,x+(w-l)/2f,y+h/2f-4,color,false);
        
    }
    
    public static void drawCenteredL10nString(String str,int x,int y,int w,int h,int color,boolean dropShadow){
        str = getRenderString(str);
        drawCenteredString(str,x,y,w,h,color,dropShadow);
    }
    
    public static String getRenderString(String unl10nStr){
        return I18n.hasKey(unl10nStr)?I18n.format(unl10nStr):unl10nStr;
    }
    
    public static void bindOBJTexture(String path){
        if(hasTexture.containsKey(path)){
            var v = hasTexture.get(path);
            if(v.getLeft()){
                Minecraft.getMinecraft().renderEngine.bindTexture(v.getRight());
            }
        }
        else {
            var resourceManager = Minecraft.getMinecraft().getResourceManager();
            var rl = new ResourceLocation(VistaRailway.MOD_ID,"models/block/"+path+".png");
            try {
                resourceManager.getResource(rl);
                hasTexture.put(path,Pair.of(true,rl));
            } catch (IOException e) {
                var str = "FileNotFound: models/block/"+path+".png. Should model have no texture?";
                LogManager.getLogger().info(str);
                ClientMessageManager.INSTANCE.debug(str);
                hasTexture.put(path,Pair.of(false,rl));
            }
            
        }
    }
}
