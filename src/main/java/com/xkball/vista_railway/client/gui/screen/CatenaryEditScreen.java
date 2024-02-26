package com.xkball.vista_railway.client.gui.screen;

import com.xkball.vista_railway.client.gui.Button;
import com.xkball.vista_railway.client.gui.component.BlockPosEditField;
import com.xkball.vista_railway.client.gui.component.GUICatenaryStyleLabel;
import com.xkball.vista_railway.client.gui.component.IntSlideBar;
import com.xkball.vista_railway.client.gui.component.Vector3fEditField;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.data.PoleTEData;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.packets.OpenCatenaryGuiPacket;
import com.xkball.vista_railway.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.function.Predicate;

import static com.xkball.vista_railway.client.gui.component.GUICatenaryStyleLabel.mouseInColor;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class CatenaryEditScreen extends VRBaseScreen {
    private final NBTTagCompound data;
    
    private final PoleTEData teData = new PoleTEData();
    
    private Vector3fEditField offsetEdit;
    private IntSlideBar yRotationEdit;

    private final BlockPosEditField[] blockPosNodes = new BlockPosEditField[4];
    
    public CatenaryEditScreen(NBTTagCompound data){
        this.data = data;
        teData.readFromNBT(data);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(teData.styleID<0){
            var str = TextFormatting.DARK_RED+I18n.format("vista_railway.catenary.not_selected");
            var l = fontRenderer.getStringWidth(str);
            fontRenderer.drawString(str,width/5-l-5,6,-1);
        }
        var rightX = width/5+width/40;
        fontRenderer.drawString(I18n.format("vista_railway.gui.y_rotation"),rightX,26,ColorUtils.white);
        fontRenderer.drawString(I18n.format("vista_railway.gui.offset"),rightX,71,ColorUtils.white);
       
        for(int i = 0;i<getNodeCountNow();i++){
            fontRenderer.drawString(I18n.format("vista_railway.gui.node_edit",I18n.format("vista_railway.gui."+(i+1))),rightX,111+40*i,ColorUtils.white);
        }
    }
    
    @Override
    public void drawBackground_() {
        drawRect(0,0,width,height, ColorUtils.background);
        drawVerticalLine(width/5,0,height,ColorUtils.white);
        drawHorizontalLine(0,width,20,ColorUtils.white);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        var label1 = new GuiLabel(Minecraft.getMinecraft().fontRenderer,nextID(),4,1,40,20,ColorUtils.white);
        var label2 = new GuiLabel(Minecraft.getMinecraft().fontRenderer,nextID(),width/5+6,1,40,20,ColorUtils.white);
        label1.addLine(I18n.format("vista_railway.gui.style_selection"));
        label2.addLine(I18n.format("vista_railway.gui.style_config"));
        labelList.add(label1);
        labelList.add(label2);
        
        int y = 21;
        for(var catenaryStyle : CatenaryDataManager.INSTANCE.catenaryDataList){
            var label = new GUICatenaryStyleLabel(catenaryStyle,0,y,width,height);
            if(teData.styleID == catenaryStyle.id()) label.selected = true;
            this.addComponent(label);
            y += label.h;
        }
        var cancel = I18n.format("vista_railway.gui.cancel");
        var apply = I18n.format("vista_railway.gui.apply");
        var confirm = I18n.format("vista_railway.gui.confirm");
        var buttonCancelL = new GuiButton(/*2*/nextID(),0,height-20,width/10,20,cancel);
        var buttonConfirmL = new GuiButton(/*3*/nextID(),width/10,height-20,width/10,20,confirm);
        var buttonCancelR = new GuiButton(/*4*/nextID(),width/5+1,height-20,width/10,20,cancel);
        var buttonApplyR = new GuiButton(/*5*/nextID(),(width/10)*3+1,height-20,width/10,20,apply);
        var buttonConfirmR = new GuiButton(/*6*/nextID(),(width/10)*4+1,height-20,width/10,20,confirm);
        buttonList.add(buttonCancelL);
        buttonList.add(buttonConfirmL);
        buttonList.add(buttonCancelR);
        buttonList.add(buttonApplyR);
        buttonList.add(buttonConfirmR);
        
        var rightX = width/5+width/40;
        offsetEdit = new Vector3fEditField(this,rightX,85,40,15,20);
        offsetEdit.init(teData.offset);
        this.addComponent(offsetEdit);
        
        yRotationEdit = new IntSlideBar(this,rightX,
                40,120,40,10,20,-180,+180,0);
        yRotationEdit.setValue(teData.yRotation);
        this.addComponent(yRotationEdit);
        
        for(int i = 0;i<blockPosNodes.length;i++){
            blockPosNodes[i] = new BlockPosEditField(this,rightX,125+40*i,40,15,20);
            blockPosNodes[i].init(teData.connections[i]);
            var nodeCount = getNodeCountNow();
            if(i>=nodeCount){
                blockPosNodes[i].setVisible(false);
            }
            else {
                var button = new RelativeBlockPosSaveButton(this,rightX+100,105+40*i,70,20,i);
                button.relative = teData.relativePos[i];
                this.addComponent(button);
            }
            this.addComponent(blockPosNodes[i]);
        }
        
    }
    
    protected void saveOffsetAndNode(){
        teData.offset = offsetEdit.get();
        for(int i = 0;i<teData.connections.length;i++){
            if(blockPosNodes[i] != null){
                teData.connections[i] = blockPosNodes[i].get();
            }
        }
        teData.yRotation = yRotationEdit.getValue();
    }
    
    protected int getNodeCountNow(){
        var style = CatenaryDataManager.INSTANCE.get(teData.styleID);
        return style!=null?style.nodeCount():0;
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button.id == 2){
            clearSelectingButton((l) -> true );
        }
        if(button.id == 3){
            for(var renderable : this.getComponents()){
                if(renderable instanceof GUICatenaryStyleLabel l && l.selecting){
                   l.selected = true;
                   l.selecting = false;
                   teData.styleID = l.id;
                   syncDataAndReinitScreen();
                }
            }
        }
        if(button.id == 4){
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        if(button.id == 5){
            saveOffsetAndNode();
            syncDataAndReinitScreen();
        }
        if(button.id == 6){
            saveOffsetAndNode();
            var pkt = new OpenCatenaryGuiPacket(teData.writeToNBT(data));
            pkt.setUpdateServer();
            pkt.closeGUI = true;
            GCNetworkManager.INSTANCE.sendPacketToServer(pkt);
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        
    }
    
    @SuppressWarnings("SpellCheckingInspection")
    private void syncDataAndReinitScreen(){
        var pkt = new OpenCatenaryGuiPacket(teData.writeToNBT(data));
        pkt.setUpdateServer();
        GCNetworkManager.INSTANCE.sendPacketToServer(pkt);
    }
    
    private void clearSelectingButton(Predicate<GUICatenaryStyleLabel> canReset){
        for(var renderable : this.getComponents()){
            if(renderable instanceof GUICatenaryStyleLabel l1){
                if(canReset.test(l1)){
                    l1.selecting = false;
                }
            }
        }
    }
    
    @Override
    protected void buttonInteraction(Button button) {
        if(button instanceof GUICatenaryStyleLabel l){
            clearSelectingButton((l1) -> l1 != l);
        }
    }
    
    public static class RelativeBlockPosSaveButton extends Gui implements Button {
        
        protected int x;
        protected int y;
        protected int w;
        protected int h;
        protected boolean relative = true;
        protected CatenaryEditScreen screen;
        protected int nodeID;
        
        public RelativeBlockPosSaveButton(CatenaryEditScreen screen,int x, int y, int w, int h,int nodeID) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.screen = screen;
            this.nodeID = nodeID;
        }
        
        @Override
        public boolean enabled() {
            return true;
        }
        
        @Override
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            Button.super.mouseClicked(mouseX, mouseY, mouseButton);
            relative = !relative;
            screen.teData.relativePos[nodeID] = relative;
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
        public void render(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
//            drawHorizontalLine(x,x+w,y,boundingLineColor);
//            drawHorizontalLine(x,x+w,y+h,boundingLineColor);
//            drawVerticalLine(x,y,y+h,boundingLineColor);
//            drawVerticalLine(x+w,y,y+h,boundingLineColor);
            String str;
            int l;
            if(relative){
                str = I18n.format("vista_railway.gui.relative_pos_save");
            }
            else {
                str = I18n.format("vista_railway.gui.absolute_pos_save");
            }
            l = minecraft.fontRenderer.getStringWidth(str);
            if(isMouseIn(mouseX,mouseY)){
                drawRect(x+w-l,y+4,x+w,y+h-4,mouseInColor);
            }
            minecraft.fontRenderer.drawString(str,x+w-l,y+h/2-4,ColorUtils.white);
           
        }
    }
}
