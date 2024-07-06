package com.woodenscalpel.buildinggizmos.common.item.palettescreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

import static net.minecraft.client.gui.GuiComponent.blit;

public class DrawTextureHelper {

    private static final Logger LOGGER = LogUtils.getLogger();

    public void drawTexture(PoseStack poseStack, int interpolatedx, int interpolatedy, int interpolatedz, Block block, Direction direction,float scale) {
        ResourceLocation texture2 = helpers.getBlockTexture(block, direction).atlas().location();

        float int3 = helpers.getBlockTexture(block, direction).getU0();
        float int4 = helpers.getBlockTexture(block, direction).getU1();
        float int5 = helpers.getBlockTexture(block, direction).getV0();
        float int6 = helpers.getBlockTexture(block, direction).getV1();
        float ratio = helpers.getBlockTexture(block, direction).uvShrinkRatio();

        int vsize = (int) ((int6 - int5) / ratio);
        int usize = (int) ((int4 - int3) / ratio);

        RenderSystem.setShaderTexture(0, texture2);
        poseStack.pushPose();
        poseStack.scale(2*scale,1*scale,1);
        //blit(poseStack, interpolatedx/2, interpolatedy, (int) (int3 / ratio), (int) (int5 / ratio), usize, vsize);

        blit(poseStack, (int) (interpolatedx/(2*scale)), (int) (interpolatedy/scale), interpolatedz, int3/ratio, int5/ratio, usize, vsize, 256, 256);
        poseStack.popPose();

    }
}
