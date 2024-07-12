package com.woodenscalpel.buildinggizmos.client;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.client.keys.KeyBinding;
import com.woodenscalpel.buildinggizmos.client.render.entity.TestEntityRenderer;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.ModeEnums;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import com.woodenscalpel.buildinggizmos.init.EntityInit;
import com.woodenscalpel.buildinggizmos.networking.Messages;
import com.woodenscalpel.buildinggizmos.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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
                    AbstractWand wand = (AbstractWand) item.getItem();

                    if (KeyBinding.MODE_SWITCH_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchPaletteMode(player);
                        Messages.sendToServer(new TextureWandModeChangePacket());
                        CompoundTag nbt = item.getOrCreateTag();
                        player.sendSystemMessage(Component.literal("Switch Palette Source to ").append(Component.translatable(AbstractWand.getPaletteSource(nbt).name)));
                    }

                    if (KeyBinding.PALLET_MENU_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).openPalletScreen();
                    }
                    if (KeyBinding.WAND_BUILD_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).build(item);
                        Messages.sendToServer(new WandBuildPacket());
                    }
                    if (KeyBinding.WAND_PLACEMENTMODE_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchPlacementMode(item);
                        CompoundTag nbt = item.getOrCreateTag();
                        player.sendSystemMessage(Component.literal("Switched Placement Mode to: ").append(Component.translatable(AbstractWand.getPlacementMode(nbt).name)));
                        Messages.sendToServer(new WandPlacementModePacket());
                    }


                }

                if (item.getItem() instanceof AbstractWand && !(item.getItem() instanceof TextureWand)) {
                    if (KeyBinding.WAND_SWAPMODE_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchSwapMode(item);
                        CompoundTag nbt = item.getOrCreateTag();
                        player.sendSystemMessage(Component.literal("Switch Swap/Place Mode to: ").append(Component.translatable(AbstractWand.getSwapMode(nbt).name)));
                        Messages.sendToServer(new WandSwapModePacket());
                    }
                    if (KeyBinding.SHAPE_SWITCH_KEY.consumeClick()) {
                        ((AbstractWand) item.getItem()).switchBuildMode(player);
                        CompoundTag nbt = item.getOrCreateTag();
                        player.sendSystemMessage(Component.literal("Switch Build Shape to: ").append(Component.translatable(AbstractWand.ShapeHelper.getShape(nbt).name)));
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
            event.register(KeyBinding.SHAPE_SWITCH_KEY);
            event.register(KeyBinding.WAND_BUILD_KEY);
            event.register(KeyBinding.WAND_PLACEMENTMODE_KEY);
            event.register(KeyBinding.WAND_SWAPMODE_KEY);
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(EntityInit.TEST.get(), TestEntityRenderer::new);
        }
    }
}
