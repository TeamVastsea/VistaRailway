package com.xkball.vista_railway.client.gui.screen;

import com.xkball.vista_railway.client.gui.Button;
import com.xkball.vista_railway.client.gui.Renderable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class VRBaseScreen extends GuiScreen {
  
    private final List<Renderable> components = new LinkedList<>();
    
    private final List<GuiTextField> textFields = new LinkedList<>();
    
    int componentID = 0;
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground_();
        super.drawScreen(mouseX, mouseY, partialTicks);
        for(var component : components){
            if(component.visible()) component.render(Minecraft.getMinecraft(),mouseX,mouseY,partialTicks);
        }
        this.executeOnVisibleTextFields(GuiTextField::drawTextBox);
    }
    
    protected void executeOnVisibleTextFields(Consumer<GuiTextField> consumer){
        for(var textField : textFields){
            if(textField.getVisible()){
                consumer.accept(textField);
            }
        }
    }
    
    //avoid override
    public void drawBackground_() {
    
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for(var component : components){
            if(component instanceof Button button && button.enabled() && button.isMouseIn(mouseX,mouseY)){
                button.mouseClicked(mouseX,mouseY,mouseButton);
                buttonInteraction(button);
            }
        }
        this.executeOnVisibleTextFields((t) -> t.mouseClicked(mouseX,mouseY,mouseButton));
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for(var component : components){
            if(component instanceof Button button && button.enabled()){
                button.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
            }
        }
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for(var component : components){
            if(component instanceof Button button && button.enabled()){
                button.mouseReleased(mouseX,mouseY,state);
            }
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.executeOnVisibleTextFields((t) -> t.textboxKeyTyped(typedChar,keyCode));
    }
    
    protected void buttonInteraction(Button button){
    
    }
    
    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn,w,h);
    }
    
    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        components.clear();
        labelList.clear();
        textFields.clear();
        componentID = 0;
        super.setWorldAndResolution(mc, width, height);
    }
    
    public int nextID(){
        var result = componentID;
        componentID++;
        return result;
    }
    
    public void addTextField(GuiTextField guiTextField){
        this.textFields.add(guiTextField);
    }
    
    public void addComponent(Renderable renderable){
        this.components.add(renderable);
        renderable.onAdd(this);
    }
    
    public List<Renderable> getComponents() {
        return components;
    }
    
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
