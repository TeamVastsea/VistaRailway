package com.xkball.vista_railway.utils;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FixedLightBlockAccess implements IBlockAccess {
    private final IBlockAccess inner;
    private final int light;
    
    public FixedLightBlockAccess(IBlockAccess inner, int light) {
        this.inner = inner;
        this.light = light;
    }
    
    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return inner.getTileEntity(pos);
    }
    
    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return inner.getCombinedLight(pos,light);
    }
    
    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return inner.getBlockState(pos);
    }
    
    @Override
    public boolean isAirBlock(BlockPos pos) {
        return inner.isAirBlock(pos);
    }
    
    @Override
    public Biome getBiome(BlockPos pos) {
        return inner.getBiome(pos);
    }
    
    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return inner.getStrongPower(pos,direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return inner.getWorldType();
    }
    
    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return inner.isSideSolid(pos,side,_default);
    }
}
