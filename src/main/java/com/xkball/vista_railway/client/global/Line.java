package com.xkball.vista_railway.client.global;

import com.xkball.vista_railway.utils.func.FloatFunction;
import com.xkball.vista_railway.utils.func.LineCreator;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final List<LineNode> nodes = new ArrayList<>();
    private final Vector3f start;
    private final Vector3f end;
    private final FloatFunction<Vector3f> line;
    
    /**
     * constructor of a line can be rendered in world.
     * @param start the start point of this line in world.
     * @param end the end point of this line in world.
     * @param line a function define domains from zero to one,considering scaling according to line length before call.
     * @param samplesCount the numbers of sample points from line,exclude end point,affects rendering performance.
     * @param sideCount the number of vertex of each sample point,affects rendering performance.
     * @param radius the radius of the line.
     */
    public Line(Vector3f start, Vector3f end, LineCreator line, int samplesCount, int sideCount, double radius){
        this(start,end,samplesCount,sideCount,radius,line.createLine(new com.xkball.vista_railway.utils.Vector3f(start),new com.xkball.vista_railway.utils.Vector3f(end)));
    }
    public Line(Vector3f start, Vector3f end, int samplesCount, int sideCount, double radius, FloatFunction<com.xkball.vista_railway.utils.Vector3f> line){
        this(start,end,samplesCount,sideCount,(f) -> line.apply(f).toVec3f(),radius);
    }
    
    public Line(Vector3f start, Vector3f end, int samplesCount, int sideCount, FloatFunction<Vector3f> line, double radius){
        this.start = start;
        this.end = end;
        this.line = line;
        Vector3f previous = null;
        Vector3f next = start;
        Vector3f delta = Vector3f.sub(end,start,new Vector3f());
        //var s = 1/(float)samplesCount;
        for(int i = 0;i<samplesCount;i++){
            var scale = (i+1)/(float)samplesCount;
            //line(0) 应该==0;
            var current = new Vector3f(next);
            var offset = line.apply(scale);
            next = Vector3f.add(start,new Vector3f(delta.x*scale,delta.y*scale,delta.z*scale), new Vector3f()).translate(offset.x,offset.y,offset.z);
            nodes.add(new LineNode(current,previous,next,sideCount,radius));
            previous = current;
        }
        var offset = line.apply(1f);
        nodes.add(new LineNode(end.translate( offset.x,offset.y,offset.z), previous,null,sideCount,radius));
    }
    
    //0<=f<=1
    public Vector3f positionAt(float f){
        Vector3f delta = Vector3f.sub(end,start,new Vector3f());
        delta.scale(f);
        var offset = line.apply(f);
        return delta.translate(offset.x+start.x,offset.y+start.y,offset.z+start.z);
    }
    @SideOnly(Side.CLIENT)
    public void render(int r,int g,int b,int a){
        if(nodes.isEmpty()) return;
        var buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        for (int i = 0; i < nodes.size()-1; i++) {
            nodes.get(i).drawToBuffer(nodes.get(i+1),buffer,r,g,b,a);
        }
        Tessellator.getInstance().draw();
    }
}
