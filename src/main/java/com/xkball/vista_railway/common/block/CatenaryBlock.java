package com.xkball.vista_railway.common.block;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.common.te.CatenaryTE;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CatenaryBlock extends Block {
    
    public static final ResourceLocation ID = new ResourceLocation(VistaRailway.MODID,"catenary");
    public CatenaryBlock() {
        super(Material.ICE);
        this.setRegistryName(ID);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new CatenaryTE();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
}
