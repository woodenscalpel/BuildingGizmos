package com.woodenscalpel.buildinggizmos.common.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab TAB_BUILDINGGIZMOS = new CreativeModeTab("buildinggizmostab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.TESTITEM.get());
        }
    };
}
