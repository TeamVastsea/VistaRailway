package com.xkball.vista_railway.client.gui.component;

import com.xkball.vista_railway.client.gui.Button;
import com.xkball.vista_railway.client.gui.Renderable;
import com.xkball.vista_railway.client.gui.screen.VRBaseScreen;
import com.xkball.vista_railway.utils.ColorUtils;
import com.xkball.vista_railway.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IntSlideBar extends Gui implements Renderable {
    protected int x;
    protected int y;
    protected int wBar;
    protected int wTextField;
    protected int wInterval;
    protected int h;
    
    protected final int minValue;
    protected final int maxValue;
    protected final int interval;
    protected int value;
    
    protected int lastPlaySound;
    
    protected GuiTextField textField;
    
    protected XDragButton button;
    
    public IntSlideBar(VRBaseScreen screen,
                       int x, int y, int wBar, int wTextField, int wInterval, int h,
                       int minValue, int maxValue,int initValue) {
        assert maxValue > minValue;
        this.x = x;
        this.y = y;
        this.wBar = wBar;
        this.wTextField = wTextField;
        this.wInterval = wInterval;
        this.h = h;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.interval = maxValue-minValue;
        initValue = MathUtils.clamp(initValue,minValue,maxValue);
        this.value = initValue-minValue;
        textField = new GuiTextField(screen.nextID(),screen.mc.fontRenderer,
                x+wBar+wInterval,y,wTextField,h){
            
            @Override
            public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
                var result = super.mouseClicked(mouseX, mouseY, mouseButton);
                if(!isFocused()){
                    try {
                        var v = Integer.parseInt(this.getText());
                        if(isValidValue(v)){
                            var bv = v-minValue;
                            if(bv != value){
                                value = bv;
                                button._x = getX()+(int)((value/(float)interval)*wBar);
                            }
                          
                        }
                    }
                    catch (NumberFormatException ignored) {
                        setText(Integer.toString(0));
                    }
                }
                return result;
            }
        };
        textField.setText(Integer.toString(initValue));
        screen.addTextField(textField);
        button = new XDragButton(x+(int)((value/(float)interval)*wBar),y+h-20,10,20);
    }
    
    public int getValue(){
        return this.minValue+this.value;
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
        return wBar+wInterval+wTextField;
    }
    
    @Override
    public int getHeight() {
        return h;
    }
    
    protected boolean isValidValue(int value){
        return value>=minValue && value<=maxValue;
    }
    
    @Override
    public void render(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        drawHorizontalLine(x,x+wBar,y+h-10,ColorUtils.white);
        drawVerticalLine(x,y+h-12,y+h-8,ColorUtils.white);
        drawVerticalLine(x+wBar,y+h-12,y+h-8,ColorUtils.white);
        var ll = minecraft.fontRenderer.getStringWidth(Integer.toString(minValue));
        var lr = minecraft.fontRenderer.getStringWidth(Integer.toString(maxValue));
        minecraft.fontRenderer.drawString(Integer.toString(minValue),x-ll/2,y,ColorUtils.white);
        minecraft.fontRenderer.drawString(Integer.toString(maxValue),x+wBar-lr/2,y,ColorUtils.white);
    }
    
    public void setValue(int value) {
        this.value = value-minValue;
        button._x = x +(int)((this.value/(float)interval)*wBar);
        textField.setText(Integer.toString(getValue()));
    }
    
    @Override
    public void onAdd(VRBaseScreen screen) {
        screen.addComponent(button);
    }
    
    public class XDragButton extends Gui implements Button{
        
        int _x;
        int _y;
        int _w;
        int _h;
        boolean dragging = false;
        int oldMouseX;
        int oldX;
        
        public XDragButton(int centerX, int y, int w, int h) {
            this._x = centerX;
            this._y = y;
            this._w = w;
            this._h = h;
            this.oldX = _x;
        }
        
        
        @Override
        public void render(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
            var _x = getX();
            drawHorizontalLine(_x,_x+_w,_y, ColorUtils.white);
            drawHorizontalLine(_x,_x+_w,_y+_h, ColorUtils.white);
            drawHorizontalLine(_x,_x+_w,_y+2, ColorUtils.white);
            drawHorizontalLine(_x,_x+_w,_y+_h-2, ColorUtils.white);
            drawVerticalLine(_x,_y,_y+_h, ColorUtils.white);
            //drawVerticalLine(_x+1,y,y+h, ColorUtils.white);
            drawVerticalLine(_x+_w,_y,_y+_h, ColorUtils.white);
            if(isMouseIn(mouseX,mouseY)){
                drawRect(_x+1,_y+1,_x+_w,_y+_h,GUICatenaryStyleLabel.mouseInColor);
            }
        }
        
        @Override
        public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
            if(clickedMouseButton != 0) return;
            if(isMouseIn(mouseX,mouseY) && !dragging){
                dragging = true;
                oldMouseX = mouseX;
                oldX = _x;
                lastPlaySound = _x;
                playPressSound();
            }
            if(dragging){
                _x = oldX+mouseX-oldMouseX;
                _x = MathUtils.clamp(_x,x,x+wBar);
                value = (int)(((float)(_x - x)/(float)wBar)*interval);
                textField.setText(Integer.toString(getValue()));
            }
            if(Math.abs(_x-lastPlaySound)>20){
                playPressSound();
                lastPlaySound=_x;
            }
        }
        
        @Override
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        
        }
        
        @Override
        public void mouseReleased(int mouseX, int mouseY, int state) {
            dragging = false;
        }
        
        @Override
        public boolean enabled() {
            return true;
        }
        
        @Override
        public int getX() {
            return _x-_w/2;
        }
        
        @Override
        public int getY() {
            return _y;
        }
        
        @Override
        public int getWidth() {
            return _w;
        }
        
        @Override
        public int getHeight() {
            return _h;
        }
        
     
    }
}
