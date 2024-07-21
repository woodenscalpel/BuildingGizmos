package com.woodenscalpel.fabric.client;

import com.woodenscalpel.fabric.client.render.GizmoWorldRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public final class ExampleModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(GizmoWorldRenderer::RenderLevelStageEvent);
        WorldRenderEvents.AFTER_TRANSLUCENT.register(GizmoWorldRenderer::RenderLevelStageEventAfterTranslucent);
    }
}
