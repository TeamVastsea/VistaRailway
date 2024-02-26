package com.xkball.vista_railway.client.gui.screen;

import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.packets.OpenCatenaryGuiPacket;
import com.xkball.vista_railway.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class CatenaryLoadingScreen extends GuiScreen {
    
    private BlockPos pos;
    public CatenaryLoadingScreen(BlockPos pos){
        this.pos = pos;
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0,0,width,height, ColorUtils.background);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        var label = new GuiLabel(Minecraft.getMinecraft().fontRenderer,0,this.width/2-20,this.height/2-10,40,20,ColorUtils.white);
        label.addLine("LOADING...");
        this.labelList.add(label);
    }
    
    @Override
    public void updateScreen() {
        if(CatenaryDataManager.INSTANCE.init){
            var te = Minecraft.getMinecraft().player.world.getTileEntity(pos);
            if(te instanceof PoleTE pe){
                GCNetworkManager.INSTANCE.sendPacketToServer(new OpenCatenaryGuiPacket(pe));
            }
        }
    }
    
    @Override
    public void handleInput() throws IOException {
        super.handleInput();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
}
