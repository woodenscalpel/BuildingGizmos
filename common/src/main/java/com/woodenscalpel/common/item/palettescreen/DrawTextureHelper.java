package com.woodenscalpel.common.item.palettescreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.misc.helpers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;


public class DrawTextureHelper {


    public static void drawTexture(GuiGraphics gui , int interpolatedx, int interpolatedy, int interpolatedz, Block block, Direction direction, float scale) {
        ResourceLocation texture2 = helpers.getBlockTexture(block, direction).atlasLocation();

        float int3 = helpers.getBlockTexture(block, direction).getU0();
        float int4 = helpers.getBlockTexture(block, direction).getU1();
        float int5 = helpers.getBlockTexture(block, direction).getV0();
        float int6 = helpers.getBlockTexture(block, direction).getV1();
        float ratio = helpers.getBlockTexture(block, direction).uvShrinkRatio();

        int vsize = (int) ((int6 - int5) / ratio);
        int usize = (int) ((int4 - int3) / ratio);

        RenderSystem.setShaderTexture(0, texture2);
        gui.pose().pushPose();
        gui.pose().scale(scale,scale,1);
        //blit(poseStack, interpolatedx/2, interpolatedy, (int) (int3 / ratio), (int) (int5 / ratio), usize, vsize);

        gui.blit((int) (interpolatedx/scale),(int) (interpolatedy/scale),interpolatedz,8,8,helpers.getBlockTexture(block,direction));
        //gui.blit(texture2, (int) (interpolatedx/(2*scale)), (int) (interpolatedy/scale), interpolatedz, int3/ratio, int5/ratio, usize, vsize, 256, 256);
        gui.pose().popPose();

    }
}
