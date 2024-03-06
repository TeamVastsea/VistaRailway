package com.xkball.vista_railway.common.item.contain_overlay;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.api.item.IPoleRenderable;
import com.xkball.vista_railway.client.gui.overlay.ConfigurableOverlay;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.NBTUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("DuplicatedCode")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BinderItem extends BaseOverlayItem implements IPoleRenderable{
    
    public static final ResourceLocation ID = new ResourceLocation(VistaRailway.MOD_ID,"binder");
    public BinderItem() {
        super(ID);
        this.setMaxStackSize(1);
        this.setTranslationKey("vista_railway.item.binder");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ConfigurableOverlay getOverlay() {
        return ConfigurableOverlay.BINDER;
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return EnumActionResult.PASS;
        var itemInHand = player.getHeldItemMainhand();
        var te = worldIn.getTileEntity(pos);
        loadToOverlay(itemInHand);
        var posRow = ConfigurableOverlay.BINDER.getRow("pos");
        if (posRow != null){
            if (player.isSneaking()) {
                if(te instanceof PoleTE) {
                    if (posRow.getColumnList().get(0).tempTag == null) {
                        posRow.getColumnList().get(0).tempTag = new NBTTagCompound();
                    }
                    //noinspection DataFlowIssue
                    NBTUtils.writeBlockPos(posRow.getColumnList().get(0).tempTag, "pos", pos);
                    
                }
                else {
                    posRow.getColumnList().get(0).tempTag = new NBTTagCompound();
                }
                saveFromOverlay(itemInHand);
                return EnumActionResult.SUCCESS;
            }
            else if (te instanceof PoleTE poleTE ) {
                var catenaryStyleRow = ConfigurableOverlay.BINDER.getRow("catenary_style");
                var nodeRow = ConfigurableOverlay.BINDER.getRow("node");
                var overrideRow = ConfigurableOverlay.BINDER.getRow("node_override");
                assert catenaryStyleRow != null && nodeRow != null && overrideRow != null;
                if(catenaryStyleRow.getCurrentSelected() == 0 || nodeRow.getCurrentSelected() == 0 || overrideRow.getCurrentSelected() == 0)  return EnumActionResult.FAIL;
                poleTE.overrideRelativePosSetting(nodeRow.getCurrentSelected()-1,overrideRow.getCurrentSelected()-1);
                poleTE.setNodeConnection(nodeRow.getCurrentSelected()-1,NBTUtils.readBlockPosOrNull(posRow.getColumnList().get(0).tempTag,"pos"));
                poleTE.data.styleID = CatenaryDataManager.INSTANCE.catenaryDataList.get(catenaryStyleRow.getCurrentSelected()-1).id();
                poleTE.markDirty();
                return EnumActionResult.SUCCESS;
            }
        }
        
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
 
}
