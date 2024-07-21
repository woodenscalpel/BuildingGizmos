package com.woodenscalpel;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.client.keys.KeyBinding;
import com.woodenscalpel.client.keys.KeypressEvent;
import com.woodenscalpel.common.item.BuildWand.BuildWand;
import com.woodenscalpel.common.item.texturewand.TextureWand;
import com.woodenscalpel.networking.PacketRegister;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.*;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.function.Supplier;

public final class BuildingGizmos {
    public static final String MOD_ID = "buildinggizmos";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> BUILDINGGIZMOS_TAB = TABS.register(
            "buildinggizmos_tab", // Tab ID
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.buildinggizmos"), // Tab Name
                    () -> new ItemStack(ITEMS.getRegistrar().get(new ResourceLocation(MOD_ID, "buildwand"))) // Icon
            )
    );

    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        // Write common init code here.

        //Register Creative Tab
        TABS.register();

        //Register items
        Registrar<Item> items = MANAGER.get().get(Registries.ITEM);

        RegistrySupplier<Item> TEXTUREWAND = items.register(new ResourceLocation(MOD_ID, "texturewand"), () -> new TextureWand(new TextureWand.Properties().arch$tab(BUILDINGGIZMOS_TAB)));
        RegistrySupplier<Item> BUILDWAND = items.register(new ResourceLocation(MOD_ID, "buildwand"), () -> new BuildWand(new BuildWand.Properties().arch$tab(BUILDINGGIZMOS_TAB)));


        /*
        //Register blocks
        Registrar<Block> blocks = MANAGER.get().get(Registries.BLOCK);
        RegistrySupplier<Block> testblock = blocks.register(new ResourceLocation(MOD_ID, "testblock"), () -> new testblock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        RegistrySupplier<BlockItem> testblockitem = items.register(new ResourceLocation(MOD_ID +"testblockitem"), () -> new BlockItem(testblock.get(),new Item.Properties().arch$tab(BUILDINGGIZMOS_TAB)));
         */


        //Register keybinds
        if(Platform.getEnv() == EnvType.CLIENT) {
            KeyBinding.register();
            ClientTickEvent.CLIENT_POST.register(KeypressEvent::tickcheckpress);
        }

        //Register packet recievers
        PacketRegister.register();
    }
}
