package com.xkball.vista_railway.client;

import com.xkball.vista_railway.mixin.VRMixinForgeBlockModelRendererAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class VRBlockModelRenderer extends ForgeBlockModelRenderer {
    public VRBlockModelRenderer(BlockColors colors) {
        super(colors);
        var access = (VRMixinForgeBlockModelRendererAccess) this;
        var buffer = new BufferBuilder(114514);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        var flat = access.getConsumerFlat().get();
        flat.setBuffer(buffer);
        flat.setOffset(BlockPos.ORIGIN);
        access.getLighterFlat().get().setParent(flat);
        var smooth = access.getConsumerSmooth().get();
        smooth.setBuffer(buffer);
        smooth.setOffset(BlockPos.ORIGIN);
        access.getLighterSmooth().get().setParent(smooth);
        buffer.reset();
    }
    
    @Override
    public boolean renderModelFlat(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buffer, boolean checkSides, long rand)
    {
        var access = (VRMixinForgeBlockModelRendererAccess) this;
        VertexBufferConsumer consumer = access.getConsumerFlat().get();
        consumer.setBuffer(buffer);
        consumer.setOffset(pos);
        
        VertexLighterFlat lighter = access.getLighterFlat().get();
        lighter.setParent(consumer);
        
        return render(lighter, world, model, state, pos, buffer, checkSides, rand);
    }
    
    @Override
    public boolean renderModelSmooth(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buffer, boolean checkSides, long rand)
    {
        var access = (VRMixinForgeBlockModelRendererAccess) this;
        VertexBufferConsumer consumer = access.getConsumerSmooth().get();
        consumer.setBuffer(buffer);
        consumer.setOffset(pos);
        
        VertexLighterSmoothAo lighter = access.getLighterSmooth().get();
        lighter.setParent(consumer);
        
        return render(lighter, world, model, state, pos, buffer, checkSides, rand);
       
    }
    
    public static boolean render(VertexLighterFlat lighter, IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder wr, boolean checkSides, long rand)
    {
        lighter.setWorld(world);
        lighter.setState(state);
        lighter.setBlockPos(pos);
        boolean empty = true;
        List<BakedQuad> quads = model.getQuads(state, null, rand);
        if(!quads.isEmpty())
        {
            lighter.updateBlockInfo();
            empty = false;
            for(BakedQuad quad : quads)
            {
                quad.pipe(lighter);
            }
        }
        for(EnumFacing side : EnumFacing.values())
        {
            quads = model.getQuads(state, side, rand);
            if(!quads.isEmpty())
            {
                if(!checkSides || state.shouldSideBeRendered(world, pos, side))
                {
                    if(empty) lighter.updateBlockInfo();
                    empty = false;
                    for(BakedQuad quad : quads)
                    {
                        quad.pipe(lighter);
                    }
                }
            }
        }
        lighter.resetBlockInfo();
        return !empty;
    }
}
