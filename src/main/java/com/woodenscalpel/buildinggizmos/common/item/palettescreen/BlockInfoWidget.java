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
import org.slf4j.Logger;

import java.util.function.Function;

public class BlockInfoWidget extends AbstractWidget {

    private static final Logger LOGGER = LogUtils.getLogger();


    Block block;
    PaletteData paletteData;
    Function callback;
    public Boolean focused;

    /*
     */
    public BlockInfoWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage, PaletteData paletteData) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.block = Blocks.STONE;
        this.paletteData = paletteData;
        this.callback = f ->{return 0;};
        this.focused = false;
    }

    public BlockInfoWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage, PaletteData paletteData, Function callback) {
        this(pX,pY,pWidth,pHeight,pMessage,paletteData);
        this.callback = callback;
      //  callback.apply(this);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        //Draw Background
        int colour = 0xEEAABBFF;
        int focuscolour = 0xBBCCDDAA;
        if(this.focused){
            colour = focuscolour;
        }
        fill(pPoseStack,x,y,width+x,y+height,colour);

        //Internal constants, Texture takes up 1/3 horizontal space, 5px padding
        int padding = 5;
        int texturemaxwidth = (this.width - padding*2)* 1/3;
        int texturemaxheight = this.height - padding*2;
        int texturewidthallowance = Math.min(texturemaxheight,texturemaxwidth);
        int texturewidthdeadspace = Math.max(texturemaxheight,texturemaxwidth);
        int textureunscaledwidth = 8; //Guessed
        float scalefactor = (float) texturewidthallowance / textureunscaledwidth;

        /*
        int textmaxwidth = (this.width - padding*2)* 2/3;
        int textmaxheight = this.height - padding*2;
        int textwidth = Math.min(textmaxheight,textmaxwidth);
         */

        int texturewidthpadding,textureheightpadding;
        if(texturemaxheight < texturemaxwidth){
            texturewidthpadding = (texturewidthdeadspace - texturewidthallowance)/2;
            textureheightpadding = 0;
        }
        else{
            textureheightpadding = (texturewidthdeadspace - texturewidthallowance)/2;
            texturewidthpadding = 0;

        }


        int texturestartx = x+padding+texturewidthpadding;
        int texturestarty = y+padding+textureheightpadding;

        //Draw Texture

        DrawTextureHelper textureHelper = new DrawTextureHelper();
        textureHelper.drawTexture(pPoseStack,texturestartx,texturestarty,0,block, Direction.SOUTH, scalefactor);

        //Draw RGB Text

        int[] rgba = paletteData.getBlockRGB(block);
        int r = rgba[0];
        int g = rgba[1];
        int b = rgba[2];

        int coordxstart = x + texturemaxwidth + padding;
        int coordystart = y + padding/2;
        int textpadding = 10;

        int fulltextheight = textpadding*3;
        int targettextheight = height - padding;
        float textscalefactor = (float) targettextheight /fulltextheight;
       // LOGGER.info("start");
       // LOGGER.info(String.valueOf(textscalefactor));
       // LOGGER.info(String.valueOf((int)100/ textscalefactor));


        //textscalefactor = 2;

        pPoseStack.pushPose();
        pPoseStack.scale(textscalefactor,textscalefactor,1);
        coordxstart = (int) (coordxstart / textscalefactor);
        coordystart = (int) (coordystart / textscalefactor);

        drawString(pPoseStack, Minecraft.getInstance().font, "Avg R:" + String.valueOf(r),coordxstart,coordystart,0xFFFFFF);
        drawString(pPoseStack, Minecraft.getInstance().font, "Avg G:" + String.valueOf(g),coordxstart,coordystart+textpadding,0xFFFFFF);
        drawString(pPoseStack, Minecraft.getInstance().font, "Avg B:" + String.valueOf(b),coordxstart,coordystart+textpadding*2,0xFFFFFF);

        pPoseStack.popPose();

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
