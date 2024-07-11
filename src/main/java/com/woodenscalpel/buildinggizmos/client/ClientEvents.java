package com.woodenscalpel.buildinggizmos.client;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.client.keys.KeyBinding;
import com.woodenscalpel.buildinggizmos.client.render.entity.TestEntityRenderer;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.init.EntityInit;
import com.woodenscalpel.buildinggizmos.networking.Messages;
import com.woodenscalpel.buildinggizmos.networking.packet.BuildWandShapeChangePacket;
import com.woodenscalpel.buildinggizmos.networking.packet.TextureWandModeChangePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
                if (item.getItem() instanceof AbstractWand) {

                    if (KeyBinding.MODE_SWITCH_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchMode(player);
                        Messages.sendToServer(new TextureWandModeChangePacket());
                    }

                    if (KeyBinding.PALLET_MENU_KEY.consumeClick()) {
                        player.sendSystemMessage(Component.literal("Pressed Pallet Menu"));
                        ((AbstractWand) item.getItem()).openPalletScreen();
                    }

                }
                if (item.getItem() instanceof BuildWand) {
                    if (KeyBinding.SHAPE_SWITCH_KEY.consumeClick()) {
                        ((BuildWand) item.getItem()).switchBuildMode(player);
                        player.sendSystemMessage(Component.literal("Switch Build Shape"));
                        Messages.sendToServer(new BuildWandShapeChangePacket());
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

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(EntityInit.TEST.get(), TestEntityRenderer::new);
        }
    }
}
