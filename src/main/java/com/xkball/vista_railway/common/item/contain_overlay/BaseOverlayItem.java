package com.xkball.vista_railway.common.item.contain_overlay;

import com.xkball.vista_railway.api.item.IKeyBoardInputRespond;
import com.xkball.vista_railway.api.item.IMouseWheelRespond;
import com.xkball.vista_railway.api.item.IOverlayProviderItem;
import com.xkball.vista_railway.client.input.KeyBoardInputHandler;
import com.xkball.vista_railway.common.item.VRBaseItem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseOverlayItem extends VRBaseItem implements IOverlayProviderItem, IKeyBoardInputRespond, IMouseWheelRespond {
    
    public BaseOverlayItem(ResourceLocation id) {
        super(id);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRespondTo(ItemStack itemStack, KeyBinding keyBinding) {
        return keyBinding == KeyBoardInputHandler.ITEM_SWITCH_MODE;
    }
    
    @Override
    public void onKeyPressed(ItemStack itemStack, int key) {
        loadToOverlay(itemStack);
        getOverlay().onKeyPressed(itemStack,key);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRespondTo(ItemStack itemStack, int dWheel) {
        loadToOverlay(itemStack);
        return getOverlay().getCurrentSelected() != 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean cancelEvent(ItemStack itemStack, int dWheel) {
        return true;
    }
    
    @Override
    public void respondToMouseWheel(EntityPlayerMP playerMP, ItemStack itemStack, int dWheel) {
        loadToOverlay(itemStack);
        getOverlay().respondToMouseWheel(playerMP,itemStack,dWheel);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderOverlay(Minecraft minecraft, ItemStack itemStack, float partialTick) {
        loadToOverlay(itemStack);
        getOverlay().render();
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        //noinspection DuplicatedCode
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.YELLOW.toString()+TextFormatting.UNDERLINE+ I18n.format("vista_railway.tooltip.configurable_hud"));
        tooltip.add(I18n.format("vista_railway.tooltip.configurable_hud.row",TextFormatting.UNDERLINE+I18n.format("key.category.vista_railway")+":"+I18n.format("vista_railway.hot_key.item_switch_mode")+TextFormatting.RESET));
        tooltip.add(I18n.format("vista_railway.tooltip.configurable_hud.column"));
    }
    
}
