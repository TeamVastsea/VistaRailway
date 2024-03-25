package com.xkball.vista_railway.common.data;

import com.xkball.vista_railway.client.global.Line;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.MathUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class CatenaryRenderData {
    public final List<Line> lines = new ArrayList<>();
    
    public boolean needUpdate = true;
    
    public CatenaryRenderData(){
    
    }
    
    @SideOnly(Side.CLIENT)
    public void readFromTE(PoleTE te){
        Vector3f[] startPointTop = new Vector3f[4];
        Vector3f[] endPointTop = new Vector3f[4];
        Vector3f[] startPointBottom= new Vector3f[4];
        Vector3f[] endPointBottom= new Vector3f[4];
        if(!te.hasWorld() || !te.getWorld().isRemote) return;
        this.needUpdate = false;
        var style = CatenaryDataManager.INSTANCE.get(te.data.styleID);
        for (int i = 0; i < 4; i++) {
            startPointTop[i] = null;
            startPointBottom[i] = null;
            endPointTop[i] = null;
            endPointBottom[i] = null;
        }
        lines.clear();
        if(!te.hasStyle()) {
            return;
        }
        for(int i=0;i<style.nodeCount();i++){
            startPointTop[i] = te.getOffsetTop(i);
            startPointBottom[i] = te.getOffsetBottom(i);
            if(te.data.relativePos[i] && te.getWorld().getTileEntity(te.getPos().add(te.data.connections[i])) instanceof PoleTE cTE && !cTE.getPos().equals(te.getPos()) && cTE.hasStyle()){
                cTE.needNotify.add(te.getPos());
                endPointTop[i] = cTE.getOffsetTop(i);
                endPointBottom[i] = cTE.getOffsetBottom(i);
            }
            else if(!te.data.relativePos[i] && te.getWorld().getTileEntity(te.data.connections[i]) instanceof PoleTE cTE && !cTE.getPos().equals(te.getPos()) && cTE.hasStyle()){
                cTE.needNotify.add(te.getPos());
                endPointTop[i] = cTE.getOffsetTop(i);
                endPointBottom[i] = cTE.getOffsetBottom(i);
            }
        }
        for (int i = 0; i < 4; i++) {
            var node = CatenaryDataManager.INSTANCE.get(te.data.styleID).nodeMap().get(i);
            if(node == null) continue;
            var lineType = node.lineType();
            var t = startPointTop[i] != null && endPointTop[i] != null && lineType.getTop().enable;
            var b = startPointBottom[i] != null && endPointBottom[i] != null && lineType.getBottom().enable;
            Line tl = null;
            Line bl = null;
            if(t){
                tl = addLine(startPointTop[i],endPointTop[i],lineType.getTop());
                //lines.add(new Line(startPointTop[i],endPointTop[i],50,6,0.05f, f-> new Vector3f(0f, (float) Math.cos(f*8),(float) Math.sin(f*8)*0.5f)));
            }
            if(b){
                bl = addLine(startPointBottom[i],endPointBottom[i],lineType.getBottom());
            }
            if(t && b && lineType == LineType.T1){
                int n = (int) (Vector3f.sub(startPointTop[i],endPointTop[i],new Vector3f()).length())/5;
                float d = 1f/ (n+1);
                for (int j = 1; j < n+1; j++) {
                    lines.add(new Line(tl.positionAt(d*j),bl.positionAt(d*j), MathUtils.LineFormat.None.offset,2,6,0.02f));
                }
            }
        }
    }
    
    public Line addLine(Vector3f start,Vector3f end,MathUtils.LineFormat format){
        var l = Vector3f.sub(end,start,new Vector3f()).length();
        var bl = new Line(start,end,format.offset, format.samples.apply(l),6,format.radius);
        lines.add(bl);
        return bl;
    }
    
    public void updateServer(PoleTE te){
        if (!te.hasStyle()) return;
        var style = CatenaryDataManager.INSTANCE.get(te.data.styleID);
        for(int i=0;i<style.nodeCount();i++){
            if(te.data.relativePos[i] && te.getWorld().getTileEntity(te.getPos().add(te.data.connections[i])) instanceof PoleTE cTE && !cTE.getPos().equals(te.getPos()) && cTE.hasStyle()){
                cTE.needNotify.add(te.getPos());
            }
            else if(!te.data.relativePos[i] && te.getWorld().getTileEntity(te.data.connections[i]) instanceof PoleTE cTE && !cTE.getPos().equals(te.getPos()) && cTE.hasStyle()){
                cTE.needNotify.add(te.getPos());
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void render(int r,int g,int b,int a){
        for (Line line : lines) {
            line.render(r,g,b,a);
        }
    }
}
