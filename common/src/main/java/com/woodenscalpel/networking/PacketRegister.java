package com.woodenscalpel.networking;

import com.woodenscalpel.BuildingGizmos;
import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.woodenscalpel.BuildingGizmos.MOD_ID;

public class PacketRegister {
    public static final ResourceLocation BUILD_PACKET_ID = new ResourceLocation(MOD_ID,"build_packet");
    public static final ResourceLocation MODESWITCH_PACKET_ID = new ResourceLocation(MOD_ID,"modeswitch_packet");
    public static final ResourceLocation SWAPMODE_PACKET_ID = new ResourceLocation(MOD_ID,"swapmode_packet");
    public static final ResourceLocation SHAPESWITCH_PACKET_ID = new ResourceLocation(MOD_ID,"shapeswitch_packet");
    public static final ResourceLocation PLACEMENTMODE_PACKET_ID = new ResourceLocation(MOD_ID,"placementmode_packet");
    public static final ResourceLocation GRADIENTMENU_PACKET_ID = new ResourceLocation(MOD_ID,"gradientmenu_packet");

    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(MOD_ID,"networkchannel"));

    public static void register(){


        NetworkManager.registerReceiver(NetworkManager.Side.C2S,BUILD_PACKET_ID, (buf, context) -> {

            Player player = context.getPlayer();
            ItemStack item = player.getMainHandItem();
            ((AbstractWand) item.getItem()).build(item);
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S,MODESWITCH_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack item = player.getMainHandItem();
            ((AbstractWand) item.getItem()).switchPaletteMode(player);
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S,SWAPMODE_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack item = player.getMainHandItem();
            ((AbstractWand) item.getItem()).switchSwapMode(item);
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S,SHAPESWITCH_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack item = player.getMainHandItem();
            ((AbstractWand) item.getItem()).switchBuildMode(player);
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S,PLACEMENTMODE_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack item = player.getMainHandItem();
            ((AbstractWand) item.getItem()).switchPlacementMode(item);
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S,GRADIENTMENU_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack item = player.getMainHandItem();
            ((AbstractWand) item.getItem()).switchPlacementMode(item);
        });

        CHANNEL.register(GradientCloseMessege.class,GradientCloseMessege::encode,GradientCloseMessege::new,GradientCloseMessege::apply);

    }
}
