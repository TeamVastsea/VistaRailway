package com.xkball.vista_railway.utils;

import com.xkball.vista_railway.VistaRailway;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ColorUtils {
    public static final int white = getColor(255,255,255,255);
    public static final int black = getColor(0,0,0,255);
    public static final int background = ColorUtils.getColor(40,40,40,155);
    public static int getColor(int r,int g,int b,int a){
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    public static int getColor(int color,int a){
        return getColor(getRed(color),getGreen(color),getBlue(color),a);
    }
    
    public static int getRed(int color){
        return color >> 16 & 255;
    }
    
    public static int getGreen(int color){
        return color >> 8 & 255;
    }
    
    public static int getBlue(int color){
        return color & 255;
    }
    
    @SideOnly(Side.CLIENT)
    public static class ClientColorUtils{
        public static int clientCurrentRed(){
            var i = VistaRailway.clientTick%(255*3);
            return Math.max(0, (i > 255) ? (510 - i) : i);
        }
        
        public static int clientCurrentGreen(){
            var i = (VistaRailway.clientTick+255)%(255*3);
            return Math.max(0, (i > 255) ? (510 - i) : i);
        }
        
        public static int clientCurrentBlue(){
            var i =( VistaRailway.clientTick+255*2)%(255*3);
            return Math.max(0, (i > 255) ? (510 - i) : i);
        }
        
    }
    
}
