package com.woodenscalpel.common.item.palettescreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.woodenscalpel.BuildingGizmos;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class DrawBG {

    static ResourceLocation menuBg = new ResourceLocation(BuildingGizmos.MOD_ID,"textures/screen/blanksquare.png");

    public static void drawBg(GuiGraphics gui, int x, int y, int width, int height){

        RenderSystem.setShaderTexture(0,menuBg);
        int textureW = 175;
        int textureH = 175;

        float scalex = (float) width/textureW;
        float scaley = (float) height/textureH;

        gui.pose().pushPose();
        gui.pose().scale((float) scalex, (float) scaley,1);

        gui.blit(menuBg,(int) (x/scalex),(int)(y/scaley), (float) 0, (float) 0, (int) (textureW),textureH,textureW,textureH);
        gui.pose().popPose();

    }
}
