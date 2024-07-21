package com.woodenscalpel.client.keys;

import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.common.item.texturewand.TextureWand;
import com.woodenscalpel.networking.PacketRegister;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class KeypressEvent {

    public static void tickcheckpress(Minecraft minecraft){
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            ItemStack item = player.getMainHandItem();
            CompoundTag nbt = item.getOrCreateTag();
            if (item.getItem() instanceof AbstractWand) {
                while (KeyBinding.MODESWITCH_KEYMAPPING.consumeClick()) {
                    ((AbstractWand) item.getItem()).switchPaletteMode(player);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    NetworkManager.sendToServer(PacketRegister.MODESWITCH_PACKET_ID,buf);
                    player.sendSystemMessage(Component.literal("Switch Palette Source to ").append(Component.translatable(AbstractWand.getPaletteSource(nbt).name)));
                }



                while (KeyBinding.BUILD_KEYMAPPING.consumeClick()) {
                    ((AbstractWand) item.getItem()).build(item);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    NetworkManager.sendToServer(PacketRegister.BUILD_PACKET_ID,buf);
                }

                while (KeyBinding.PLACEMENTMODE_KEYMAPPING.consumeClick()) {
                    ((AbstractWand) item.getItem()).switchPlacementMode(item);
                    player.sendSystemMessage(Component.literal("Switched Placement Mode to: ").append(Component.translatable(AbstractWand.getPlacementMode(nbt).name)));
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    NetworkManager.sendToServer(PacketRegister.PLACEMENTMODE_PACKET_ID,buf);
                }

                while (KeyBinding.PALETTEMENU_KEYMAPPING.consumeClick()) {
                    ((AbstractWand) item.getItem()).openPaletteScreen();
                }

                if (item.getItem() instanceof AbstractWand && !(item.getItem() instanceof TextureWand)) {
                    while (KeyBinding.SHAPESWITCH_KEYMAPPING.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchBuildMode(player);
                        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                        NetworkManager.sendToServer(PacketRegister.SHAPESWITCH_PACKET_ID,buf);
                        player.sendSystemMessage(Component.literal("Switch Build Shape to: ").append(Component.translatable(AbstractWand.ShapeHelper.getShape(nbt).name)));
                    }
                    while (KeyBinding.SWAPMODE_KEYMAPPING.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchSwapMode(item);
                        player.sendSystemMessage(Component.literal("Switch Swap/Place Mode to: ").append(Component.translatable(AbstractWand.getSwapMode(nbt).name)));
                        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                        NetworkManager.sendToServer(PacketRegister.SWAPMODE_PACKET_ID,buf);
                    }
                }
            }
        }
    }
}