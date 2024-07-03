package com.woodenscalpel.buildinggizmos.client;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.client.keys.KeyBinding;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import com.woodenscalpel.buildinggizmos.networking.Messages;
import com.woodenscalpel.buildinggizmos.networking.packet.TextureWandModeChangePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = BuildingGizmos.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents{

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {

            Player player = Minecraft.getInstance().player;
            if(player != null) {
                ItemStack item = player.getMainHandItem();
                if (item.getItem() instanceof TextureWand) {

                    if (KeyBinding.MODE_SWITCH_KEY.consumeClick()) {
                        //player.sendSystemMessage(Component.literal("Pressed Mode Switch"));
                        ((TextureWand) item.getItem()).switchMode(player);
                        Messages.sendToServer(new TextureWandModeChangePacket());
                    }

                    if (KeyBinding.PALLET_MENU_KEY.consumeClick()) {
                        player.sendSystemMessage(Component.literal("Pressed Pallet Menu"));
                        ((TextureWand) item.getItem()).openPalletScreen();
                    }

                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BuildingGizmos.MODID, value = Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBinding.MODE_SWITCH_KEY);
            event.register(KeyBinding.PALLET_MENU_KEY);
        }
    }
}
