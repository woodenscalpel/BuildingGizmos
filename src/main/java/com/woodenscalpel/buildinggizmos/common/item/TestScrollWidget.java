package com.woodenscalpel.buildinggizmos.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class TestScrollWidget extends AbstractScrollWidget {
    private static final Logger LOGGER = LogUtils.getLogger();

    int contentHeight = 1000;

    public TestScrollWidget(int p_240025_, int p_240026_, int p_240027_, int p_240028_, Component p_240029_) {
        super(p_240025_, p_240026_, p_240027_, p_240028_, p_240029_);
    }


    @Override
    protected int getInnerHeight() {
        return 500;
    }

    @Override
    protected boolean scrollbarVisible() {
        return true;
    }

    @Override
    protected double scrollRate() {
        return 10;
    }

    @Override
    protected void renderContents(PoseStack p_239198_, int p_239199_, int p_239200_, float p_239201_) {

        fill(p_239198_,10,10,300,100,0xAAAAAAAA);
        LOGGER.info("RENDER CONTENT");


    }

    @Override
    public void renderButton(PoseStack p_239793_, int p_239794_, int p_239795_, float p_239796_) {

        if (this.visible) {
            enableScissor(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1);
            p_239793_.pushPose();
            p_239793_.translate(0.0D, -this.scrollAmount(), 0.0D);
            this.renderContents(p_239793_,p_239794_,p_239795_,p_239796_);
            p_239793_.popPose();
            disableScissor();
            this.renderDecorations(p_239793_);
        }
        //super.renderButton(p_239793_, p_239794_, p_239795_, p_239796_);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, Minecraft pMinecraft, int pMouseX, int pMouseY) {
        super.renderBg(pPoseStack, pMinecraft, pMouseX, pMouseY);
    }

    @Override
    protected void renderDecorations(PoseStack p_239981_) {
        LOGGER.info("RENDER DECO");
        LOGGER.info(String.valueOf(this.getInnerHeight()));
        //fill(p_239981_,10,10,150,300,0xAAAAAAAA);
        super.renderDecorations(p_239981_);
    }


    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
