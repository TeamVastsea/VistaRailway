package com.xkball.vista_railway.client.render.line;

import com.xkball.vista_railway.utils.function.FloatUnaryOperator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;


public class Line {
    
    public static final Vector3f UNIT = new Vector3f(1,0,0);
    
    private final Vector3f start;
    //private final Vector3f end;
    private final Vector3f line;
    //private final double length;
    
    //private final FloatUnaryOperator fx;
  //  private final FloatUnaryOperator derivative_fx;
    
    private final Node[] nodes;
    
    public Line(Vector3f start, Vector3f end, int steps, FloatUnaryOperator fx, boolean isServer){
        this.start = start;
        //this.end = end;
        var length = Vector3f.sub(end,start,null).length();
        nodes = new Node[steps+1];
        //this.fx = fx;
        this.line = Vector3f.sub(end,start,null);
        if(isServer) return;
        
        //移动到原始fx值
        for(int i=0;i<steps+1;i++){
            var x = (length/steps)*i;
            nodes[i] = Node.withX(x);
            nodes[i].setY(fx.applyAsFloat(x));
        }
        
        //计算渲染顶点
        for(int i=0;i<nodes.length;i++){
            nodes[i].computeFinalPoint(getOrNull(i-1),getOrNull(i+1));
            
        }
        for (Node node : nodes) {
            node.end();
        }
    }
    
    public Line(Vector3f start, Vector3f end,BezierCurve bezierCurve,boolean isServer){
        this.start = start;
        this.line = Vector3f.sub(end,start,null);
        this.nodes = bezierCurve.getPoints();
        if(isServer) return;
        //计算渲染顶点
        for(int i=0;i<nodes.length;i++){
            nodes[i].computeFinalPoint(getOrNull(i-1),getOrNull(i+1));
            
        }
        for (Node node : nodes) {
            node.end();
        }
    }
    
    @Nullable
    private Node getOrNull(int i){
        if(i>=0 && i<nodes.length){
            return nodes[i];
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void render(BufferBuilder bufferBuilder,int r,int g,int b,int a){
        //ro.normalise(ro);
//        var rzp = Vector2f.angle(Node.UNIT,new Vector2f(ro.x,ro.y));
//        var ryn = Vector2f.angle(Node.UNIT,new Vector2f(ro.x,ro.z));
        if(line.y != 0 || line.z != 0 ){
            var roDegree = Math.toDegrees(Vector3f.angle(line,UNIT));
            var axis = Vector3f.cross(UNIT,line,null);
            GlStateManager.translate(start.x,start.y,start.z);
            GL11.glTranslatef(0,0,0);
            GlStateManager.rotate((float) roDegree,axis.x,axis.y,axis.z);
        }
        else {
            GlStateManager.translate(start.x,start.y,start.z);
            if(line.x<0){
                GL11.glTranslatef(0,0,0);
                GlStateManager.rotate(180F,0F,1F,0F);
            }
         
        }
        GlStateManager.scale(0.08f,0.08f,0.08f);
        //XkballRailway.ModEventHandler.clientTick%
        for(int i=0;i< nodes.length-1;i++){
            
            var self = nodes[i];
            var next = nodes[i+1];
            
            for(int j=0;j<6;j++){
                int k = j+1>5?0:j+1;
                putVertex(bufferBuilder,self.pos[j],self.normal[j],r,g,b,a,0,0);
                putVertex(bufferBuilder,next.pos[j],next.normal[j],r,g,b,a,0,1);
                putVertex(bufferBuilder,next.pos[k],next.normal[k],r,g,b,a,1,1);
                putVertex(bufferBuilder,self.pos[k],self.normal[k],r,g,b,a,1,0);
            }
            
        }
        
        
        
    }
    @SideOnly(Side.CLIENT)
    public void putVertex(BufferBuilder bufferBuilder,Vector3f vertex,Vector3f normal,int r,int g,int b,int a,double u,double v){
        bufferBuilder.pos(vertex.x, vertex.y, vertex.z)
                .normal(normal.x, normal.y, normal.z)
                .tex(u,v)
                .color(r,g,b,a).endVertex();
    }
}
