package com.xkball.vista_railway.common.item.contain_overlay;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.api.item.IKeyBoardInputRespond;
import com.xkball.vista_railway.api.item.IMouseWheelRespond;
import com.xkball.vista_railway.api.item.IOverlayProviderItem;
import com.xkball.vista_railway.api.item.IPoleRenderable;
import com.xkball.vista_railway.client.gui.overlay.ConfigurableOverlay;
import com.xkball.vista_railway.client.input.KeyBoardInputHandler;
import com.xkball.vista_railway.common.item.VRBaseItem;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.AssertUtils;
import com.xkball.vista_railway.utils.ColorUtils;
import com.xkball.vista_railway.utils.NBTUtils;
import com.xkball.vista_railway.utils.RenderUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Deprecated
public abstract class BinderItemOld extends VRBaseItem implements IOverlayProviderItem, IPoleRenderable, IKeyBoardInputRespond, IMouseWheelRespond {
    
    public static final ResourceLocation ID = new ResourceLocation(VistaRailway.MOD_ID,"binder");
    public BinderItemOld() {
        super(ID);
        this.setMaxStackSize(1);
        this.setTranslationKey("vista_railway.item.binder");
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderOverlay(Minecraft minecraft, ItemStack itemStack, float partialTick) {
        var tag = AssertUtils.requireNonNullElseGet(itemStack.getTagCompound(),NBTTagCompound::new);
//        GlStateManager.enableBlend();
//        var selected = tag.getInteger("selected");
//        var pos1 = NBTUtils.readBlockPosOrNull(tag,"pos_s");
//        var node = tag.hasKey("node_id")?tag.getInteger("node_id"):-1;
//        var nodeStateOverride = NodeSaveOverrideState.readFromNBT(tag);
//        var l = Math.min(Math.min(minecraft.displayHeight/10,minecraft.displayWidth/20),40);
//        nodeStateOverride.render(10,l+40,l,selected==2);
//        var nodeOverlay = new OverlaySelection(I18n.format("vista_railway.gui.connecting_node"),l,selected==1,node);
//        for(int i=1;i<5;i++){
//            nodeOverlay.addSelection(i-1,I18n.format("vista_railway.gui."+i));
//        }
//        nodeOverlay.render(10,40);
//        var posOverlay = new OverlaySelection(I18n.format("vista_railway.gui.connecting_pos"),l*3,false,-1);
//        posOverlay.setH(l);
//        posOverlay.addSelection(1,pos1==null?"":pos1.toString())
//                .render(10,40+l*2);
//
        ConfigurableOverlay.BINDER.readFromNBT(tag);
        ConfigurableOverlay.BINDER.render();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRespondTo(ItemStack itemStack,KeyBinding keyBinding) {
        return keyBinding == KeyBoardInputHandler.ITEM_SWITCH_MODE;
    }
    
    @Override
    public void onKeyPressed(ItemStack itemStack, int key) {
        var tag = AssertUtils.requireNonNullElseGet(itemStack.getTagCompound(),NBTTagCompound::new);
        var selected = tag.getInteger("selected");
        selected++;
        selected%=3;
        tag.setInteger("selected",selected);
        itemStack.setTagCompound(tag);
    }
    
    @Override
    public boolean canRespondTo(ItemStack itemStack, int DWheel) {
        var tag = AssertUtils.requireNonNullElseGet(itemStack.getTagCompound(),NBTTagCompound::new);
        var selected = tag.getInteger("selected");
        return selected>0;
    }
    
    @Override
    public boolean cancelEvent(ItemStack itemStack, int DWheel) {
        return true;
    }
    
    @Override
    public void respondToMouseWheel(EntityPlayerMP playerMP, ItemStack itemStack, int dWheel) {
        var tag = AssertUtils.requireNonNullElseGet(itemStack.getTagCompound(),NBTTagCompound::new);
        var selected = tag.getInteger("selected");
        if (selected==1) {
            if (!tag.hasKey("node_id")) {
                tag.setInteger("node_id",0);
            }
            else {
                var node = tag.getInteger("node_id");
                if (dWheel < 0) {
                    node++;
                } else {
                    node+=3;
                }
                node%=4;
                tag.setInteger("node_id",node);
                itemStack.setTagCompound(tag);
            }
        }
        else if (selected==2) {
            NodeSaveOverrideState.readFromNBT(tag).relative(dWheel<0).writeToNBT(tag);
            itemStack.setTagCompound(tag);
        }
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return EnumActionResult.PASS;
        var itemInHand = player.getHeldItemMainhand();
        var te = worldIn.getTileEntity(pos);
        if(te instanceof PoleTE poleTE){
            var tag = AssertUtils.requireNonNullElseGet(itemInHand.getTagCompound(),NBTTagCompound::new);
            if (player.isSneaking()) {
                NBTUtils.writeBlockPos(tag,"pos_s",pos);
            }
            else if(tag.hasKey("pos_S")
                    && tag.hasKey("node_id")
                    && NodeSaveOverrideState.readFromNBT(tag) != NodeSaveOverrideState.UNSET){
                var id = tag.getInteger("node_id");
                //poleTE.overrideRelativePosSetting(id, NodeSaveOverrideState.readFromNBT(tag));
                poleTE.setNodeConnection(id,NBTUtils.readBlockPos(tag,"pos_s"));
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.YELLOW.toString()+TextFormatting.UNDERLINE+I18n.format("vista_railway.tooltip.configurable_hud"));
        tooltip.add(I18n.format("vista_railway.tooltip.configurable_hud.row",I18n.format("key.category.vista_railway")+":"+I18n.format("vista_railway.hot_key.item_switch_mode")));
        tooltip.add(I18n.format("vista_railway.tooltip.configurable_hud.column"));
    }
    
    public enum NodeSaveOverrideState{
        UNSET(0){
            @Override
            public NodeSaveOverrideState relative(boolean down) {
                return NONE;
            }
        },
        NONE(1){
            @Override
            public NodeSaveOverrideState relative(boolean down) {
                return down?RELATIVE:ABSOLUTE;
            }
        },
        RELATIVE(2){
            @Override
            public NodeSaveOverrideState relative(boolean down) {
                return values()[id+(down?1:-1)];
            }
        },
        ABSOLUTE(3){
            @Override
            public NodeSaveOverrideState relative(boolean down) {
                return down?NONE:RELATIVE;
            }
        };
        
        public final int id;
        public static final int TEXT_COLOR = ColorUtils.getColor(202,218,252,255);
        
        NodeSaveOverrideState(int id) {
            this.id = id;
        }
        
        public static NodeSaveOverrideState readFromNBT(NBTTagCompound tagCompound){
            var state = tagCompound.hasKey("node_state_override")?tagCompound.getInteger("node_state_override"):0;
            return values()[state];
        }
        
        abstract NodeSaveOverrideState relative(boolean down);
       
        public void writeToNBT(NBTTagCompound tagCompound){
            tagCompound.setInteger("node_state_override",id);
        }
        
        @SideOnly(Side.CLIENT)
        public void render(int x, int y, int l,boolean selected){
            var title = I18n.format("vista_railway.gui.node_state_override");
            var l0 = Minecraft.getMinecraft().fontRenderer.getStringWidth(title)+8;
            RenderUtils.drawItemOverlayBG(x,y,l0,l,61,96,172,200);
            RenderUtils.drawItemOverlayBG(x+l0,y,l*3,l,60,172,255,200);
            if(this.id!=0){
                RenderUtils.renderBoundary(x+l*(id-1)+l0,y,l,l,61,96,172,200);
            }
            if(selected){
                RenderUtils.renderBoundary(x-1,y-1,l0+2,l+2,61,96,172,255);
            }
            RenderUtils.drawCenteredString(title,x,y,l0,l,TEXT_COLOR,true);
            RenderUtils.drawCenteredString(I18n.format("vista_railway.gui.node_state_override.none"),x+l0,y,l,l,TEXT_COLOR,id==1);
            RenderUtils.drawCenteredString(I18n.format("vista_railway.gui.node_state_override.relative"),x+l0+l,y,l,l,TEXT_COLOR,id==2);
            RenderUtils.drawCenteredString(I18n.format("vista_railway.gui.node_state_override.absolute"),x+l0+l*2,y,l,l,TEXT_COLOR,id==3);
        }
    }
}
