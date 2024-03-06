package com.xkball.vista_railway.client.gui.component;

import com.github.bsideup.jabel.Desugar;
import com.xkball.vista_railway.utils.ColorUtils;
import com.xkball.vista_railway.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
@Deprecated
public class OverlaySelection {
    public static final int TEXT_COLOR = ColorUtils.getColor(202,218,252,255);
    private final List<Selection> selections = new ArrayList<>();
    private final String title;
    private final int l;
    private int h;
    private final int l0;
    private final boolean selected;
    private final int selectedID;
    
    public OverlaySelection(String title, int l, boolean selected, int selectedID) {
        this.title = title;
        this.l = l;
        this.h = l;
        this.selected = selected;
        this.selectedID = selectedID;
        var fontRender = Minecraft.getMinecraft().fontRenderer;
        this.l0 = fontRender.getStringWidth(title)+8;
    }
    
    public OverlaySelection addSelection(int id,String contents){
        selections.add(new Selection(id,contents));
        return this;
    }
    
    public void render(int x,int y){
        var size = selections.size();
        RenderUtils.drawItemOverlayBG(x,y,l0,h,61,96,172,200);
        RenderUtils.drawItemOverlayBG(x+l0,y,l*size,h,60,172,255,200);
        if(selected){
            RenderUtils.renderBoundary(x-1,y-1,l0+2,h+2,61,96,172,255);
        }
        RenderUtils.drawCenteredString(title,x,y,l0,h,TEXT_COLOR,true);
        for(int i=0;i<size;i++){
            var b = selectedID == selections.get(i).id;
            RenderUtils.drawCenteredString(selections.get(i).contents,x+l0+l*i,y,l,h,TEXT_COLOR,b);
            if(b){
                RenderUtils.renderBoundary(x+l0+l*i,y,l,h,61,96,172,255);
            }
        }
    }
    
    public void setH(int h) {
        this.h = h;
    }
    
    @Desugar
    private record Selection(int id, String contents){
    
    }
}
