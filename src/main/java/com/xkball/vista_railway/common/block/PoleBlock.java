package com.xkball.vista_railway.common.block;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.api.item.IOverlayProviderItem;
import com.xkball.vista_railway.client.gui.screen.CatenaryLoadingScreen;
import com.xkball.vista_railway.common.data.CatenaryDataManager;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.network.GCNetworkManager;
import com.xkball.vista_railway.network.packets.OpenCatenaryGuiPacket;
import com.xkball.vista_railway.network.packets.RequestCatenaryDataPacket;
import com.xkball.vista_railway.registration.Register;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PoleBlock extends BlockContainer {
    
    public static final ResourceLocation ID = new ResourceLocation(VistaRailway.MOD_ID,"pole");
    
    public PoleBlock() {
        super(Material.ANVIL);
        this.setRegistryName(ID);
        this.setCreativeTab(Register.VR_TAB);
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        var te = worldIn.getTileEntity(pos);
        if(te instanceof PoleTE poleTE){
                for (BlockPos blockPos : poleTE.needNotify) {
                if(worldIn.getTileEntity(blockPos) instanceof PoleTE poleTE1){
                    poleTE1.sentDataToClient(PoleTE.SAVE);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote){
            activatedClient(worldIn,playerIn,pos);
            return true;
        }
        return false;
    }
    @SideOnly(Side.CLIENT)
    public void activatedClient(World worldIn, EntityPlayer playerIn, BlockPos pos){
        if(worldIn.isRemote && !(playerIn.getHeldItemMainhand().getItem() instanceof IOverlayProviderItem)){
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
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullBlock(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
    
    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return Block.NULL_AABB;
    }
    
//    @Override
//    @SuppressWarnings("deprecation")
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return EMPTY_AABB;
//    }
//
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
//        if(worldIn.isRemote){
//            var player = Minecraft.getMinecraft().player;
//            if(player.getHeldItemMainhand().getItem() != VRItems.POLE_BLOCK_ITEM && player.getHeldItemOffhand().getItem() != VRItems.POLE_BLOCK_ITEM){
//                return EMPTY_AABB;
//            }
//        }
        return Block.FULL_BLOCK_AABB;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue(IBlockState state)
    {
        return 1.0F;
    }
    
}
