package com.woodenscalpel.buildinggizmos.common.item;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BuildingGizmos.MODID);

    public static final RegistryObject<Item> TESTITEM = ITEMS.register("testitem", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TAB_BUILDINGGIZMOS)));
    public static final RegistryObject<Item> TEXTUREWAND= ITEMS.register("texturewand", () -> new TextureWand(new TextureWand.Properties().tab(ModCreativeModeTab.TAB_BUILDINGGIZMOS)));
    public static final RegistryObject<Item> PALLETWAND= ITEMS.register("palletwand", () -> new PalletWand(new PalletWand.Properties().tab(ModCreativeModeTab.TAB_BUILDINGGIZMOS)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
