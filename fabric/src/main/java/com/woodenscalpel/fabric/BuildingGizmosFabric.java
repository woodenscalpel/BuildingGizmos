package com.woodenscalpel.fabric;

import dev.architectury.registry.registries.RegistrarManager;
import net.fabricmc.api.ModInitializer;

import com.woodenscalpel.BuildingGizmos;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;

public final class BuildingGizmosFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        BuildingGizmos.init();
    }
}
