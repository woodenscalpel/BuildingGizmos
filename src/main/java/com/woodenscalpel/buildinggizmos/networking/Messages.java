package com.woodenscalpel.buildinggizmos.networking;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.networking.packet.BuildWandShapeChangePacket;
import com.woodenscalpel.buildinggizmos.networking.packet.TextureWandModeChangePacket;
import com.woodenscalpel.buildinggizmos.networking.packet.TextureWandPalletClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;

    private static int id(){
        return packetID++;
    }

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(BuildingGizmos.MODID, "nessages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(TextureWandModeChangePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TextureWandModeChangePacket::new)
                .encoder(TextureWandModeChangePacket::toBytes)
                .consumerMainThread(TextureWandModeChangePacket::handle).add();

        net.messageBuilder(TextureWandPalletClosePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TextureWandPalletClosePacket::decode)
                .encoder(TextureWandPalletClosePacket::toBytes)
                .consumerMainThread(TextureWandPalletClosePacket::handle).add();

        net.messageBuilder(BuildWandShapeChangePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BuildWandShapeChangePacket::new)
                .encoder(BuildWandShapeChangePacket::toBytes)
                .consumerMainThread(BuildWandShapeChangePacket::handle).add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),message);

    }

}
