package com.xkball.vista_railway.common.block;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.client.gui.screen.CatenaryLoadingScreen;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.packets.OpenCatenaryGuiPacket;
import com.xkball.vista_railway.network.packets.RequestCatenaryDataPacket;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PoleBlock extends BlockContainer {
    
    public static ResourceLocation ID = new ResourceLocation(VistaRailway.MOD_ID,"pole");
    
    public PoleBlock() {
        super(Material.ANVIL);
        this.setRegistryName(ID);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setTranslationKey("vista_railway.block.pole");
    }
    @Override
    @SuppressWarnings("deprecation")
    public boolean hasTileEntity() {
        return true;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return super.getRenderType(state);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new PoleTE();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote){
            if(!CatenaryDataManager.INSTANCE.init){
                GCNetworkManager.INSTANCE.sendPacketToServer(new RequestCatenaryDataPacket());
                Minecraft.getMinecraft().displayGuiScreen(new CatenaryLoadingScreen(pos));
                
            }
            else {
                var te = worldIn.getTileEntity(pos);
                if(te instanceof PoleTE pe){
                    GCNetworkManager.INSTANCE.sendPacketToServer(new OpenCatenaryGuiPacket(pe));
                }
            
            }
        }
        return true;
    }
}
