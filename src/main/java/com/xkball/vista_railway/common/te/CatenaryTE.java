package com.xkball.vista_railway.common.te;

import com.xkball.vista_railway.client.render.line.BezierCurve;
import com.xkball.vista_railway.client.render.line.Line;
import com.xkball.vista_railway.utils.RenderUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CatenaryTE extends SyncedTE{
    
    public BlockPos next = BlockPos.ORIGIN;
    public Vector3f point2 = new Vector3f(0,0,0);
    public Vector3f point3 = new Vector3f(0,0,0);
    public Vector3f point4 = new Vector3f(0,0,0);
    public boolean render = false;
    
    public Line toRender = null;
    
    @Override
    public void writeInitialSyncData(PacketBuffer buffer) {
        super.writeInitialSyncData(buffer);
        buffer.writeBlockPos( next );
        buffer.writeBoolean(render);
        writePoint(point2,buffer);
        writePoint(point3,buffer);
        writePoint(point4,buffer );
    }
    
    @Override
    public void receiveInitialSyncData(PacketBuffer buffer) {
        super.receiveInitialSyncData(buffer);
        next = buffer.readBlockPos();
        render = buffer.readBoolean();
        point2 = readPoint(buffer);
        point3 = readPoint(buffer);
        point4 = readPoint(buffer);
//        toRender = new Line(RenderUtils.vec3fFromPos(pos),RenderUtils.vec3fFromPos(next),1000,
//                //(x) -> (float) (Math.sin(x*2)*40/x)
//                (x) -> (float) (Math.sin(x*5)*x*5)
//                ,!getWorld().isRemote);
        var point1 = RenderUtils.vec3fFromPosMid(pos);
        toRender =  new Line(point1,RenderUtils.vec3fFromPosMid(next),
                new BezierCurve(100,point1,point2,point3,point4),!getWorld().isRemote);
        if(!this.getWorld().isRemote){
            writeCustomData(0,this::writeInitialSyncData);
        }
        else{
            this.scheduleRenderUpdate();
        }
    }
    
    @Override
    public void receiveCustomData(int var1, PacketBuffer var2) {
        super.receiveCustomData(var1, var2);
        if(this.getWorld().isRemote){
            this.receiveInitialSyncData(var2);
            this.scheduleRenderUpdate();
        }
    }
    
    
    @Override
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared();
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().down(),render?next:getPos().up());
    }
    
    private static void writePoint(Vector3f vec,PacketBuffer buffer){
        buffer.writeFloat(vec.x);
        buffer.writeFloat(vec.y);
        buffer.writeFloat(vec.z);
    }
    
    private static Vector3f readPoint(PacketBuffer buffer){
        var x = buffer.readFloat();
        var y = buffer.readFloat();
        var z = buffer.readFloat();
        return new Vector3f(x,y,z);
    }
}
