package com.woodenscalpel.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.woodenscalpel.BuildingGizmos;

@Mod(BuildingGizmos.MOD_ID)
public final class BuildingGizmosForge {
    public BuildingGizmosForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(BuildingGizmos.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        BuildingGizmos.init();
    }
}
