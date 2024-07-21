package com.woodenscalpel.common.item.palettescreen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class QuantizedScrollSubWidget extends AbstractWidget {

    /*
    Scrollbar that has N positions and returns N. N must be calculated outside of this function and passed to it.
     */

    private static final Logger LOGGER = LogUtils.getLogger();

    int scrolldrawposoffset;
    public int scrollslot;

    int Nslots;

    public QuantizedScrollSubWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage, int Nslots) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.Nslots = Nslots;
        this.scrollslot = 0;
        this.scrolldrawposoffset = Math.max((this.height)/(Nslots+1),1);
    }

    public int getN(){
        return Nslots;
    }

    public void setN(int n) {
        this.Nslots = n;
        this.scrolldrawposoffset = Math.max((this.height)/(n+1),1);
    }

    int getNfromPos(int pos) {
        int N = (pos - this.getY()) / scrolldrawposoffset;
        if (N < 0) {
            return 0;}
         else return Math.min(N, Nslots);
    }

    void scrolltovisualpos(int pos){
        int N = getNfromPos(pos);
        scrolltoN(N);
    }

    public void scrolltoN(int n) {
        this.scrollslot = n;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(this.getX(),this.getY(),this.getX()+this.width,this.getY()+this.height,0xCCBBCCCC);

        //LOGGER.info(String.valueOf(scrolldrawposoffset));
        int scrollbarypos = this.getY()+scrolldrawposoffset*scrollslot;

        guiGraphics.fill(this.getX(),scrollbarypos,this.getX()+this.width,scrollbarypos+this.scrolldrawposoffset,0xff11e0FF);

        //fill(pPoseStack);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }


    protected void renderBg(PoseStack pPoseStack, Minecraft pMinecraft, int pMouseX, int pMouseY) {
        //super.renderBg(pPoseStack, pMinecraft, pMouseX, pMouseY);
    }


    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        scrolltovisualpos((int) pMouseY);
    }

    @Override
    protected void onDrag(double pMouseX, double pMouseY, double pDragX, double pDragY) {
        super.onDrag(pMouseX, pMouseY, pDragX, pDragY);
        scrolltovisualpos((int) pMouseY);
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
