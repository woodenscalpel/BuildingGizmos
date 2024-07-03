package com.woodenscalpel.buildinggizmos.common.item.palettescreen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInfoWidget2 extends AbstractWidget {

    private static final Logger LOGGER = LogUtils.getLogger();


    Block block;
    PaletteData paletteData;
    Function callback;
    public Boolean focused;

    /*
     */
    public BlockInfoWidget2(int pX, int pY, int pWidth, int pHeight, Component pMessage, PaletteData paletteData) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.block = Blocks.STONE;
        this.paletteData = paletteData;
        this.callback = f ->{return 0;};
        this.focused = false;
    }

    public BlockInfoWidget2(int pX, int pY, int pWidth, int pHeight, Component pMessage, PaletteData paletteData, Function callback) {
        this(pX,pY,pWidth,pHeight,pMessage,paletteData);
        this.callback = callback;
      //  callback.apply(this);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        int colour = 0xEEAABBFF;
        int focuscolour = 0xBBCCDDAA;
        if(this.focused){
            colour = focuscolour;
        }
        fill(pPoseStack,x,y,width+x,y+height,colour);
        DrawTextureHelper textureHelper = new DrawTextureHelper();
        textureHelper.drawTexture(pPoseStack,x+10,y+10,0,block, Direction.SOUTH, 3.6F);

        int[] rgba = paletteData.getBlockRGB(block);
        int r = rgba[0];
        int g = rgba[1];
        int b = rgba[2];

        int coordxstart = x + 40;
        int coordystart = y + 10;
        int coordypad = 10;


        //pPoseStack.pushPose();
        //pPoseStack.scale(0.5F,1,1);
        //pPoseStack.translate(coordxstart,0,0);

        drawString(pPoseStack, Minecraft.getInstance().font, "Avg R:" + String.valueOf(r),coordxstart,coordystart,0xFFFFFF);
        drawString(pPoseStack, Minecraft.getInstance().font, "Avg G:" + String.valueOf(g),coordxstart,coordystart+coordypad,0xFFFFFF);
        drawString(pPoseStack, Minecraft.getInstance().font, "Avg B:" + String.valueOf(b),coordxstart,coordystart+coordypad*2,0xFFFFFF);

        //pPoseStack.popPose();

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        callback.apply(this);
        //LOGGER.info("Clicked Blockinfo");
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, Minecraft pMinecraft, int pMouseX, int pMouseY) {
        //super.renderBg(pPoseStack, pMinecraft, pMouseX, pMouseY);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        //super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return super.charTyped(pCodePoint, pModifiers);
    }
}
