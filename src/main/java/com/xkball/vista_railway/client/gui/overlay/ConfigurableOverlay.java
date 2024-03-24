package com.xkball.vista_railway.client.gui.overlay;

import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.NBTUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConfigurableOverlay {
    
    public static final Supplier<List<Pair<String,Integer>>> STYLE_LIST =
            () -> CatenaryDataManager.INSTANCE.catenaryDataList
                    .stream()
                    .map(s -> Pair.of("vista_railway.catenary.name."+s.name(),s.id()))
                    .collect(Collectors.toList());
    public static final ConfigurableOverlay BINDER = new ConfigurableOverlay()
//            .addRow(new OverlayRow("catenary_style",true)
//                    .setRenderString("vista_railway.gui.style_selection")
//                    .addAll(STYLE_LIST.get()))
            .addRow(new OverlayRow("node",true)
                    .setRenderString("vista_railway.gui.connecting_node")
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.1"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.2"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.3"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.4")))
            .addRow(new OverlayRow("node_override",true)
                    .setRenderString("vista_railway.gui.node_state_override")
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.node_state_override.none"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.node_state_override.relative"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("vista_railway.gui.node_state_override.absolute")))
            .addRow(new OverlayRow("pos",false)
                    .setRenderString("vista_railway.gui.connecting_pos")
                    .addOverlayColumn(new OverlayColumn()
                            .setTagName("pos",(self,tag) -> NBTUtils.readBlockPos(tag,"pos").toString())
                            .setW(120)
                            ));
    
    public static final ConfigurableOverlay TRANSLATOR = new ConfigurableOverlay()
            .addRow(new OverlayRow("axis",true)
                    .setRenderString("vista_railway.gui.axis")
                    .addOverlayColumn(new OverlayColumn().setRenderString("X"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("Y"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("Z")))
            .addRow(new OverlayRow("interval",true)
                    .setRenderString("vista_railway.gui.interval")
                    .addOverlayColumn(new OverlayColumn().setRenderString("1.0"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("0.5"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("0.25"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("0.125"))
                    .addOverlayColumn(new OverlayColumn().setRenderString("0.0625")))
            .addRow(new OverlayRowNBTEditor("offset")
                    .setMouseWheelRespond(
                            (self,tag,i) -> {
                                var pos = NBTUtils.readVec3f(tag,"pos");
                                var axis = Objects.requireNonNull(self.getOverlay().getRow("axis"))
                                        .getCurrentSelected()-1;
                                int d = Objects.requireNonNull(self.getOverlay().getRow("interval"))
                                        .getCurrentSelected()-1;
                                d = 1<< (4-d);
                                float offset = d/16f;
                                if (i>0) {
                                    offset=-offset;
                                }
                                var wPos = pos.translate(axis==0?offset:0,axis==1?offset:0,axis==2?offset:0);
                                NBTUtils.writeVec3f(tag,"pos",wPos);
                                if (self.getOverlay().playerMP != null && self.getOverlay().additionalData.hasKey("pos_x")) {
                                    var tePos = NBTUtils.readBlockPos(self.getOverlay().additionalData,"pos");
                                    var world = self.getOverlay().playerMP.world;
                                    if(world.getTileEntity(tePos) instanceof PoleTE poleTE){
                                        poleTE.setOffset(wPos);
                                    }
                                }
                            })
                    .setRenderString("vista_railway.gui.translator.offset")
                    .addOverlayColumn(new OverlayColumn()
                            .setW(120)
                            .setTagName("unused",
                            (self,tag) ->
                                    //很不优雅 但是暂时没办法
                                    NBTUtils.readVec3f(self.getRow().saveToNBT(new NBTTagCompound()).getCompoundTag("tag"),"pos").toString()))
            );
            
          
    private final Map<String, OverlayRow> renderButtonMap = new HashMap<>();
    private final List<OverlayRow> renderButton = new ArrayList<>();
    private final List<OverlayRow> selectableButton = new ArrayList<>();
    private NBTTagCompound additionalData = new NBTTagCompound();
    private EntityPlayerMP playerMP = null;
    private int currentSelected = 0;
    
    public int x =10;
    public int y = 40;
    public int dy = 0;
    public int minW = 40;
    public int minH = 40;
    
    public ConfigurableOverlay(){
    
    }
    
    public ConfigurableOverlay addRow(OverlayRow buttonRow){
        renderButton.add(buttonRow);
        renderButtonMap.put(buttonRow.getTagName(),buttonRow);
        buttonRow.setOverlay(this);
        if(buttonRow.selectable()){
            selectableButton.add(buttonRow);
            buttonRow.setRenderID(selectableButton.size());
        }
        return this;
    }
    
    public void readFromNBT(NBTTagCompound tag){
        if(tag.hasKey("current_selected")){
            currentSelected = tag.getInteger("current_selected");
            if(currentSelected>selectableButton.size()) currentSelected=0;
        }
        if(tag.hasKey("additionalData")){
            this.additionalData = tag.getCompoundTag("additionalData");
        }
        for (OverlayRow overlayRow : renderButton) {
            var name = overlayRow.getTagName();
            if(tag.hasKey(name)) overlayRow.readFromNBT(tag.getCompoundTag(name));
        }
    }
    
    public NBTTagCompound saveToNBT(NBTTagCompound tag){
        tag.setInteger("current_selected",currentSelected);
        tag.setTag("additionalData",additionalData);
        for (var overlayRow : renderButton) {
            var name = overlayRow.getTagName();
            tag.setTag(name, overlayRow.saveToNBT(new NBTTagCompound()));
        }
        return tag;
    }
    
    public void onKeyPressed(ItemStack itemStack, int key) {
        currentSelected++;
        currentSelected%= (renderButton.size()+1);
        var tag = NBTUtils.getOrCreateTag(itemStack);
        itemStack.setTagCompound(this.saveToNBT(tag));
    }
    
    public void respondToMouseWheel(EntityPlayerMP playerMP, ItemStack itemStack, int dWheel){
        this.playerMP = playerMP;
        this.selectableButton.get(currentSelected-1).respondToMouseWheel(playerMP,itemStack,dWheel);
        var tag = NBTUtils.getOrCreateTag(itemStack);
        itemStack.setTagCompound(this.saveToNBT(tag));
        this.playerMP = null;
    }
    
    @Nullable
    public OverlayRow getRow(String name){
        return renderButtonMap.get(name);
    }
    
    public boolean hasRow(String name){
        return renderButtonMap.containsKey(name);
    }
    
    @SideOnly(Side.CLIENT)
    public void render(){
        int ry = y;
        for (OverlayRow buttonRow : renderButton) {
            buttonRow.render(x, ry, minW, minH, currentSelected);
            ry += minH + dy;
        }
    }
    
    public int getCurrentSelected() {
        return currentSelected;
    }
    
    public NBTTagCompound getAdditionalData() {
        return additionalData;
    }
    
    public void setAdditionalData(NBTTagCompound additionalData) {
        this.additionalData = additionalData;
    }
    
}
