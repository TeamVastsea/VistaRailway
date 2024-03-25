package com.xkball.vista_railway.common.item.contain_overlay;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.api.item.IPoleRenderable;
import com.xkball.vista_railway.client.gui.overlay.ConfigurableOverlay;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.NBTUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TranslatorItem extends BaseOverlayItem implements IPoleRenderable {
    
    public static final ResourceLocation ID = new ResourceLocation(VistaRailway.MOD_ID,"translator");
    
    public TranslatorItem() {
        super(ID);
        this.setMaxStackSize(1);
        this.setTranslationKey("vista_railway.item.translator");
    }
    
    @Override
    public ConfigurableOverlay getOverlay() {
        return ConfigurableOverlay.TRANSLATOR;
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return EnumActionResult.PASS;
        var itemInHand = player.getHeldItemMainhand();
        var te = worldIn.getTileEntity(pos);
        loadToOverlay(itemInHand);
        if(player.isSneaking()){
            if (te instanceof PoleTE poleTE) {
                var tag = getOverlay().getAdditionalData();
                NBTUtils.writeBlockPos(tag,"pos",poleTE.getPos());
                poleTE.setOffset(NBTUtils.readVec3f(Objects.requireNonNull(getOverlay().getRow("offset")).saveToNBT(new NBTTagCompound()).getCompoundTag("tag"),"pos"));
                player.sendStatusMessage(new TextComponentTranslation("vista_railway.item_action.binder.bind0"),true);
            }
            else {
                getOverlay().setAdditionalData(new NBTTagCompound());
                player.sendStatusMessage(new TextComponentTranslation("vista_railway.item_action.binder.bind1"),true);
            }
            saveFromOverlay(itemInHand);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
}
