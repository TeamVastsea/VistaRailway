package com.xkball.vista_railway.client.global;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LineNode {
    private final int sideCount;
    private final Vector3f center;
    private final BlockPos renderBlockpos;
    
    private final List<Vector3f> points = new ArrayList<>(){
        @Override
        public Vector3f get(int index) {
            if(index==this.size()) index=0;
            return super.get(index);
        }
    };
    
    public LineNode(Vector3f center, @Nullable Vector3f previous,@Nullable Vector3f next, int sideCount, double radius) {
        assert previous != null || next != null;
        this.sideCount = sideCount;
        this.center = center;
        this.renderBlockpos = new BlockPos(center.x,center.y,center.z);
        
        Vector3f facing;
        if(previous == null)
            facing = Vector3f.sub(next,center,new Vector3f());
        else if(next == null)
            facing = Vector3f.sub(center,previous,new Vector3f());
        else {
            facing = Vector3f.sub(next,previous,new Vector3f());
        }
        facing.normalise(facing);
        
        //noinspection SuspiciousNameCombination
        var u = new Vector3f(-facing.y,facing.x,0);
        u.normalise(u);
        fixFacing(u);
        var v = Vector3f.cross(facing,u,new Vector3f());
        //其实好像不用归一化
        v.normalise(v);
        if(facing.x == 0 && facing.y == 0){
            u = new Vector3f(1,0,0);
            v = new Vector3f(0,1,0);
        }
        float step = (float) (2*Math.PI/sideCount);
        for(int i=0;i<sideCount;i++){
            Vector3f pu = (Vector3f) new Vector3f(u).scale((float) (Math.cos(step*i)*radius));
            Vector3f pv = (Vector3f) new Vector3f(v).scale((float) (Math.sin(step*i)*radius));
            Vector3f.add(pu,pv,pu);
            points.add(Vector3f.add(pu,center,pu));
        }
    }
    
    public int getSideCount() {
        return sideCount;
    }
    
    @SuppressWarnings("DuplicatedCode")
    @SideOnly(Side.CLIENT)
    public void drawToBuffer(@Nullable LineNode next,BufferBuilder bufferBuilder,int r,int g,int b,int a){
        if(next == null || next.sideCount != this.sideCount || Minecraft.getMinecraft().world == null) return;
        var lightS = Minecraft.getMinecraft().world.getLightFor(EnumSkyBlock.SKY,renderBlockpos);
        var lightB = Minecraft.getMinecraft().world.getLightFor(EnumSkyBlock.BLOCK,renderBlockpos);
        for(var i = 0;i<sideCount;i++){
            var p1 = points.get(i);
            var p2 = next.points.get(i);
            var p3 = next.points.get(i+1);
            var p4 = points.get(i+1);
            bufferBuilder.pos(p1.x,p1.y,p1.z).tex(0,0).lightmap(lightS,lightB).color(r,g,b,a).endVertex();
            bufferBuilder.pos(p2.x,p2.y,p2.z).tex(1,0).lightmap(lightS,lightB).color(r,g,b,a).endVertex();
            bufferBuilder.pos(p3.x,p3.y,p3.z).tex(1,1).lightmap(lightS,lightB).color(r,g,b,a).endVertex();
            bufferBuilder.pos(p4.x,p4.y,p4.z).tex(0,1).lightmap(lightS,lightB).color(r,g,b,a).endVertex();
            
        }
    }
    
    //效果存疑
    public void fixFacing(Vector3f facing){
//        if(facing.x == 0 && facing.y < 0){
//            facing.y = -facing.y;
//        }
        //else
        if(facing.y == 0 && facing.x < 0){
            facing.x = -facing.x;
        }
    }
}
