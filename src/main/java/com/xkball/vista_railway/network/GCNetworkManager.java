package com.xkball.vista_railway.network;

import com.xkball.vista_railway.VistaRailway;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.internal.LogManagerStatus;

import java.io.IOException;

//borrow from https://harbinger.covertdragon.team/chapter-07/forge-extension/fml-event-channel.html
//@Mod.EventBusSubscriber
public enum GCNetworkManager {
    
    INSTANCE;
    
    //获得一个信道实例。建议使用Modid来命名。
    //当然也可以用别的，保证唯一即可。
    private final FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(VistaRailway.MOD_ID);
    
    
    GCNetworkManager() {
        channel.register(this);
        //VistaRailway.logger.debug("VistaRailway NetworkManager setup");
    }
    
    public static void init(){
    
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientPacketEvent(FMLNetworkEvent.ClientCustomPacketEvent event) {
        IThreadListener threadListener = FMLCommonHandler.instance().getWorldThread(event.getHandler());
        if(threadListener.isCallingFromMinecraftThread()){
            decodeDataClient(event.getPacket().payload(),Minecraft.getMinecraft().player);
        }
        else {
            threadListener.addScheduledTask(() ->
                    decodeDataClient(event.getPacket().payload(), Minecraft.getMinecraft().player));
        }
        //decodeDataClient(event.getPacket().payload(), Minecraft.getMinecraft().player);
    }
    
    @SubscribeEvent
    public void onServerPacketEvent(FMLNetworkEvent.ServerCustomPacketEvent event) {
        //VistaRailway.logger.debug("server receive packet");
        IThreadListener threadListener = FMLCommonHandler.instance().getWorldThread(event.getHandler());
        if(threadListener.isCallingFromMinecraftThread()){
            decodeDataServer(event.getPacket().payload(), ((NetHandlerPlayServer)event.getHandler()).player);
        }
        else {
            threadListener.addScheduledTask(() ->
                    decodeDataServer(event.getPacket().payload(), ((NetHandlerPlayServer)event.getHandler()).player));
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void decodeDataClient(ByteBuf input, EntityPlayerSP player) {
        // 服务器->客户端的解包逻辑
        GCPacket packet = GCPacketFactory.INSTANCE.getPacket(input.readByte(),input);
        packet.onClient(player);
    }
    
    private void decodeDataServer(ByteBuf input, EntityPlayerMP player) {
        // 客户端->服务器的解包逻辑
        GCPacket packet = GCPacketFactory.INSTANCE.getPacket(input.readByte(),input);
        packet.onServer(player);
        
    }
    
    
    //向某个维度发包
    public void sendPacketToDim(GCPacket pkt, int dim) {
        channel.sendToDimension(createFMLProxyPacket(pkt), dim);
    }
    
    //向某个维度的某个点发包
    public void sendPacketAroundPos(GCPacket pkt, int dim, BlockPos pos) {
        channel.sendToAllAround(createFMLProxyPacket(pkt), new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 2.0D));
    }
    
    //向某个玩家发包
    public void sendPacketToPlayer(GCPacket pkt, EntityPlayerMP player) {
        channel.sendTo(createFMLProxyPacket(pkt), player);
    }
    
    //向所有人发包
    public void sendPacketToAll(GCPacket pkt) {
        channel.sendToAll(createFMLProxyPacket(pkt));
    }
    
    //向服务器发包，这个给客户端用
    public void sendPacketToServer(GCPacket pkt) {
        channel.sendToServer(createFMLProxyPacket(pkt));
    }
    
    //FMLEventChannel经由这个NetworkHandler暴露出来的方法到此为止
    
    private FMLProxyPacket createFMLProxyPacket(GCPacket pkt) {
        ByteBuf buffer = Unpooled.buffer();
        try {
            buffer.writeByte(GCPacketFactory.TYPE_TABLE.get(pkt.getClass()).ordinal());
            pkt.writeData(buffer);
        } catch (IOException e) {
            LogManager.getLogger().error(e);
        }
        return new FMLProxyPacket(new PacketBuffer(buffer), VistaRailway.MOD_ID);
    }
}

