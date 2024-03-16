package com.xkball.vista_railway.common.data;

import com.xkball.vista_railway.client.global.Line;
import com.xkball.vista_railway.common.te.PoleTE;
import com.xkball.vista_railway.utils.MathUtils;
import com.xkball.vista_railway.utils.func.FloatFunction;
import com.xkball.vista_railway.utils.func.FloatUnaryOperator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class CatenaryRenderData {
    public final Vector3f[] startPointTop = new Vector3f[4];
    public final Vector3f[] endPointTop = new Vector3f[4];
    public final Vector3f[] startPointBottom= new Vector3f[4];
    public final Vector3f[] endPointBottom= new Vector3f[4];
    public final List<Line> lines = new ArrayList<>();
    
    public boolean needUpdate = true;
    
    public CatenaryRenderData(){
    
    }
    
    public void readFromTE(PoleTE te){
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
            var t = startPointTop[i] != null && endPointTop[i] != null;
            var b = startPointBottom[i] != null && endPointBottom[i] != null;
            Line tl = null;
            Line bl = null;
            if(t){
                tl = new Line(startPointTop[i],endPointTop[i], f->0f,5,6,0.05f);
                lines.add(tl);
                //lines.add(new Line(startPointTop[i],endPointTop[i],50,6,0.05f, f-> new Vector3f(0f, (float) Math.cos(f*8),(float) Math.sin(f*8)*0.5f)));
            }
            if(b){
                var l = Vector3f.sub(endPointBottom[i],startPointBottom[i],new Vector3f()).length();
                bl = new Line(startPointBottom[i],endPointBottom[i], MathUtils.createCatenary(5,l/6), (int) (l*4),8,0.045f);
                lines.add(bl);
            }
            if(t && b){
                int n = (int) (Vector3f.sub(startPointTop[i],endPointTop[i],new Vector3f()).length())/5;
                float d = 1f/(n+1);
                for (int j = 0; j < n+1; j++) {
                    lines.add(new Line(tl.positionAt(d*j),bl.positionAt(d*j),f->0,2,8,0.045f));
                }
            }
        }
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
