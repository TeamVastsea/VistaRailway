package com.xkball.vista_railway.client.gui.component;

import com.xkball.vista_railway.client.gui.Renderable;
import com.xkball.vista_railway.client.gui.screen.VRBaseScreen;
import com.xkball.vista_railway.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public abstract class Vector3EditField<T> implements Renderable {
    
    protected GuiTextField textFieldX;
    protected GuiTextField textFieldY;
    protected GuiTextField textFieldZ;
    
    protected int x;
    protected int y;
    protected int wTextField;
    protected int wInterval;
    protected int h;
    
    protected boolean visible = true;
    
    
    abstract public T get();
    abstract public void init(T value);
    
    abstract public boolean validInput(String str);
    
    public Vector3EditField(VRBaseScreen screen,int x,int y,int wTextField,int wInterval,int h){
        this.x = x;
        this.y = y;
        this.wTextField = wTextField;
        this.wInterval = wInterval;
        this.h = h;
        this.textFieldX = new GuiTextField(screen.nextID(), Minecraft.getMinecraft().fontRenderer,x+wInterval,y,wTextField,h);
        this.textFieldY = new GuiTextField(screen.nextID(), Minecraft.getMinecraft().fontRenderer,x+wTextField+wInterval*2,y,wTextField,h);
        this.textFieldZ = new GuiTextField(screen.nextID(), Minecraft.getMinecraft().fontRenderer,x+wTextField*2+wInterval*3,y,wTextField,h);
        textFieldX.setValidator(this::validInput);
        textFieldY.setValidator(this::validInput);
        textFieldZ.setValidator(this::validInput);
        screen.addTextField(textFieldX);
        screen.addTextField(textFieldY);
        screen.addTextField(textFieldZ);
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
        return wTextField*3+wInterval*2;
    }
    
    @Override
    public int getHeight() {
        return h;
    }
    
    @Override
    public void render(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        int stringY = y+h/2-4;
        var l = minecraft.fontRenderer.getStringWidth("X:")+4;
        minecraft.fontRenderer.drawString("X:",x+wInterval-l,stringY, ColorUtils.white);
        minecraft.fontRenderer.drawString("Y:",x+wInterval*2-l+wTextField,stringY, ColorUtils.white);
        minecraft.fontRenderer.drawString("Z:",x+wInterval*3-l+wTextField*2,stringY, ColorUtils.white);
    }
    
    @Override
    public boolean visible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
        textFieldX.setVisible(visible);
        textFieldY.setVisible(visible);
        textFieldZ.setVisible(visible);
    }
    
    public static float parseAsFloat(GuiTextField textField) {
        try {
            return Float.parseFloat(textField.getText());
        }
        catch (NumberFormatException e) {
            textField.setText("0");
            return 0.0F;
        }
    }
    
    public static int parseAsInt(GuiTextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        }
        catch (NumberFormatException e) {
            textField.setText("0");
            return 0;
        }
    }
}
