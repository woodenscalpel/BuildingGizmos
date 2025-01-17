package com.woodenscalpel.common.item.palettescreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.BuildingGizmos;
import com.woodenscalpel.misc.SetBlockInterface;
import com.woodenscalpel.misc.helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class ExplorerWidgetCallback extends AbstractWidget {

    int maxx;
    int maxy;
    PaletteData paletteData;

    float rotx;
    float roty;
    float rotz;

//keycodes
    static final int up = 265;
    static final int down = 264;
    static final int left = 263;
    static final int right = 262;

    enum COORDMODE{
        RGB
    }
    List<Pair<Block, Vec3>> absoluteCoords;
    List<Pair<Block, Vec3>> screenCoords;


    COORDMODE mode;

    //PaletteScreenContainer.setBlock callback;
    SetBlockInterface.setBlock callback;

    public ExplorerWidgetCallback(int pX, int pY, int pWidth, int pHeight, Component pMessage, PaletteData paletteData, SetBlockInterface.setBlock callback) {
        super(pX, pY, pWidth, pHeight, pMessage);

        this.height = pHeight;
        this.width = pWidth;
        this.maxx = pX + pWidth;
        this.maxy = pY + pHeight;

        this.paletteData = paletteData;
        this.mode = COORDMODE.RGB;
        this.absoluteCoords = getAbsoluteCoords();
        this.rotx = 0;
        this.roty = 0;
        this.rotz = 0;
        this.screenCoords = updateScreenCoords();
        this.callback = (SetBlockInterface.setBlock) callback;
    }

    private List<Pair<Block,Vec3>> getAbsoluteCoords() {
        switch (mode){
            case RGB:
                return (List<Pair<Block,Vec3>>) paletteData.getBlockRGBlist().stream().map(p -> new Pair<>(p.getFirst(), rgba2rgbcoord(p.getSecond()))).collect(Collectors.toList());
            default:
                return null;
        }
    }

    private Vec3 cameraTransform(Vec3 worldCoords) {
        int centerx = ((maxx-getX())/2)+getX();
        int centery = height/2+getY();
        int centerz = width/2;
        Vec3 centervec = new Vec3(centerx,centery,centerz);


        //Vec3 testrotvec = new Vec3(testrotx, testroty, testrotz);

        //Vec3 newcoord = testRot(new int[] {r,g,b},new int[] {centerx,centerx,centerx},testrotvec);

        int interpolatedx = (int) Math.round((worldCoords.x)*(maxx-getX())+getX());
        int interpolatedy = (int) Math.round((worldCoords.y)*(maxy-getY())+getY());
        int interpolatedz = (int) Math.round((worldCoords.z)*(width));

        Vec3 scaled = new Vec3(interpolatedx,interpolatedy,interpolatedz);
        Vec3 rotated = scaled.add(centervec.multiply(-1,-1,-1)).xRot(rotx).yRot(roty).zRot(rotz).add(centervec);

        int finx = (int) rotated.x;
        int finy = (int) rotated.y;
        int finz = (int) rotated.z;

        return new Vec3(finx,finy,finz);

    }

    private List<Pair<Block,Vec3>> updateScreenCoords() {

        return (List<Pair<Block,Vec3>>) this.absoluteCoords.stream().map(p -> new Pair<>(p.getFirst(), cameraTransform(p.getSecond()))).collect(Collectors.toList());
    }

    private Vec3 rgba2rgbcoord(int[] second) {
        return new Vec3(second[0]/255.0,second[1]/255.0,second[2]/255.0);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderBg(guiGraphics, Minecraft.getInstance(),mouseX,mouseY);


        for(Pair<Block, Vec3> pair : screenCoords){
            drawTexture(guiGraphics,pair.getFirst(),pair.getSecond());
        }


    }

    private void renderBg(GuiGraphics guiGraphics, Minecraft instance, int mouseX, int mouseY) {

        guiGraphics.fill(getX(),getY(),this.getX()+this.width,this.getY()+this.height,0xCCCCCCCC);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    private void drawTexture(GuiGraphics poseStack,Block block, Vec3 coord) {
        DrawTextureHelper.drawTexture(poseStack, (int) coord.x, (int) coord.y, (int) coord.z,block,Direction.EAST,0.7F);
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
       super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {

        LOGGER.info("KEY PRESSED");

        float changeamt = 0.1F;

        switch (pKeyCode){
            case up:
                rotx = rotx + changeamt;
                break;
            case down:
                rotx = rotx - changeamt;
                break;
            case left:
                roty = roty - changeamt;
                break;
            case right:
                roty = roty + changeamt;
                break;

        }

        screenCoords = updateScreenCoords();

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        //return super.keyReleased(pKeyCode, pScanCode, pModifiers);
        return false;
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        int texturesize =8;
        super.onClick(pMouseX, pMouseY);
        for(Pair<Block, Vec3> pair : screenCoords){
            int minx = (int) pair.getSecond().x;
            int miny = (int) pair.getSecond().y;
            int maxx = (int) minx+texturesize;
            int maxy = (int) miny+texturesize;
            if(minx < pMouseX && pMouseX < maxx && miny < pMouseY && pMouseY < maxy){
                //LOGGER.info(String.valueOf(pair.getFirst()));

                //parent.selectedBlock = pair.getFirst();
                callback.set(pair.getFirst());

            }

        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


}
