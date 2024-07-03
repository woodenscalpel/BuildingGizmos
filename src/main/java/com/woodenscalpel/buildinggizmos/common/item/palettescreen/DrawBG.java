package com.woodenscalpel.buildinggizmos.common.item.palettescreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.gui.GuiComponent.blit;

public class DrawBG {

    static ResourceLocation menuBg = new ResourceLocation(BuildingGizmos.MODID,"textures/screen/blanksquare.png");

    public static void drawBg(PoseStack pPoseStack, int x, int y, int width, int height){

        RenderSystem.setShaderTexture(0,menuBg);
        int textureW = 175;
        int textureH = 175;

        float scalex = (float) width/textureW;
        float scaley = (float) height/textureH;

        pPoseStack.pushPose();
        pPoseStack.scale((float) scalex, (float) scaley,1);

        //pPoseStack.scale((float) width, (float) height,1);
        //blit(pPoseStack, (int) (x/scalex),(int) (y/scaley),0,0,175,175);
        blit(pPoseStack,(int) (x/scalex),(int)(y/scaley), (float) 0, (float) 0, (int) (textureW),textureH,textureW,textureH);
        pPoseStack.popPose();

    }
}
