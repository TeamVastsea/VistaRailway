package com.xkball.vista_railway.common.item;

import com.xkball.vista_railway.VistaRailway;
import com.xkball.vista_railway.common.block.CatenaryBlock;
import com.xkball.vista_railway.common.te.CatenaryTE;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TestItem extends Item {
    
    //public static final ResourceLocation ID = new ResourceLocation(XkballRailway.MODID,"test");
    
    public final int index;
    
    public TestItem(int index){
        this.index = index;
        this.setRegistryName(new ResourceLocation(VistaRailway.MODID,"test_"+index));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            var itemInHand = player.getHeldItem(hand);
            var block = worldIn.getBlockState(pos).getBlock();
            if(block instanceof CatenaryBlock && itemInHand.hasTagCompound()) {
                assert itemInHand.getTagCompound() != null;
                if (itemInHand.getTagCompound().hasKey("x")) {
                    var tag = itemInHand.getTagCompound();
                    var x = tag.getInteger("x");
                    var y = tag.getInteger("y");
                    var z = tag.getInteger("z");
                    var posTo = new BlockPos(x,y,z);
                    var te = worldIn.getTileEntity(pos);
                    if(te == null) return EnumActionResult.PASS;
                    if(te instanceof CatenaryTE cte){
                        var vec = new Vector3f(x+0.5f,y+0.5f,z+0.5f);
                        if(index == 4){
                            cte.next = posTo;
                            cte.render = true;
                            cte.point4 = vec;
                        }
                        if(index == 3) cte.point3 = vec;
                        if(index == 2) cte.point2 = vec;
                        
                        cte.writeCustomData(0,cte::writeInitialSyncData);
                        cte.markDirty();
                        
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
            else {
                var tag = itemInHand.getTagCompound() == null? new NBTTagCompound():itemInHand.getTagCompound();
                tag.setInteger("x",pos.getX());
                tag.setInteger("y",pos.getY());
                tag.setInteger("z",pos.getZ());
                itemInHand.setTagCompound(tag);
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
