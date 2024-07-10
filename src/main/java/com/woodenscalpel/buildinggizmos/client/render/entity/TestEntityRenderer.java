package com.woodenscalpel.buildinggizmos.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.woodenscalpel.buildinggizmos.common.entity.TestEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class TestEntityRenderer extends EntityRenderer<TestEntity> {
    public TestEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(TestEntity pEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @Override
    public void render(TestEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
