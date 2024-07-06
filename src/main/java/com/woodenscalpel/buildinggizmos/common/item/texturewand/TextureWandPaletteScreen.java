package com.woodenscalpel.buildinggizmos.common.item.texturewand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.common.item.palettescreen.*;
import com.woodenscalpel.buildinggizmos.misc.SetBlockInterface;
import com.woodenscalpel.buildinggizmos.networking.Messages;
import com.woodenscalpel.buildinggizmos.networking.packet.TextureWandPalletClosePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextureWandPaletteScreen extends Screen {

    private static final Logger LOGGER = LogUtils.getLogger();

    int borderpadding = 5;

    PaletteData paletteData;
    ExplorerWidgetCallback explorerWidget;

    BlockInfoWidget selectedBlockInfoWidget;

    ScrollWidgetList testScrollWidget;
    List<Block> testBlockList = new ArrayList<>();

    SearchWidget searchWidget;

    public Block selectedBlock;
    Block searchResult;

    ResourceLocation menuBg = new ResourceLocation(BuildingGizmos.MODID,"textures/screen/blanksquare.png");


    //Positoning Values,
    int framepadding = 10;
    //Selected Block Frame
    int selectedblockframeX,selectedblockframeY,selectedblockframeW,selectedblockframeH;
    int searchFrameX,searchFrameY,searchFrameW,searchFrameH;
    int paletteFrameX, paletteFrameY, paletteFrameW, paletteFrameH;

    int textsize = 9;
    //Search Frame
    //Pallet Frame



    public TextureWandPaletteScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        /*
        RenderSystem.setShaderTexture(0,menuBg);

        int padding = 10;
        int selectorBgX = width/2 + padding;
        int selectorBgY =  padding;
        int selectorW = width/2 - 2*padding;
        int selectorH = 50;

        pPoseStack.pushPose();
        float scalex  = (float) selectorW /175;
        float scaley = (float) selectorH /175;
        pPoseStack.scale((float) scalex, (float) scaley,1);
        //blit(pPoseStack, (int) (selectorBgX/scalex), (int) (selectorBgY/scaley)+100,0,0,175,175);
        pPoseStack.popPose();
        //blit(pPoseStack,selectorBgX,selectorBgY,0,0,selectorW,selectorH,175,175);

        //DrawBG.drawBg(pPoseStack,selectorBgX,selectorBgY,selectorW,selectorH);
        LOGGER.info(String.valueOf(selectorBgX));
        LOGGER.info(String.valueOf(width));
        LOGGER.info(String.valueOf(selectedblockframeX));

         */
        DrawBG.drawBg(pPoseStack,this.selectedblockframeX,selectedblockframeY,selectedblockframeW,selectedblockframeH);
        DrawBG.drawBg(pPoseStack,searchFrameX,searchFrameY,searchFrameW,searchFrameH);
        DrawBG.drawBg(pPoseStack, paletteFrameX, paletteFrameY, paletteFrameW, paletteFrameH);
        drawCenteredString(pPoseStack,Minecraft.getInstance().font,"Selected Block",selectedblockframeX+(selectedblockframeW/2),selectedblockframeY+1,0xFFFFFF);
        drawCenteredString(pPoseStack,Minecraft.getInstance().font,"Gradient Selection",searchFrameX+(searchFrameW/2),searchFrameY+3,0xFFFFFF);
        drawCenteredString(pPoseStack,Minecraft.getInstance().font,"Current Block Palette", paletteFrameX +(paletteFrameW /2), paletteFrameY +3,0xFFFFFF);

        //fill(pPoseStack,selectedblockframeX-50,selectorBgY,selectedblockframeX+10,selectedblockframeY+9,0xAAAAAAAA);

        //fill(pPoseStack,selectedblockframeX,selectedblockframeY,selectedblockframeX+1,selectedblockframeY+9,0xAAAAAAAA);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }


    @Override
    protected void init() {
        //get block palette data (once)
        this.paletteData = new PaletteData();
        this.selectedBlock = Blocks.STONE;
        this.searchResult = Blocks.STONE;
        ItemStack wand = Minecraft.getInstance().player.getMainHandItem();
        this.testBlockList = ((TextureWand) wand.getItem()).getPaletteBlocks(wand);

        //Explorer widget in top right quadrant of screen
        //needs to be a square, with badding around edge
        int explorerreigonwidth = width/2;
        int explorerreigonheight = height/2;
        int explorerwidth = (explorerreigonwidth-borderpadding*2);
        int explorerheight = (explorerreigonheight-borderpadding*2);
        int explorerdimension = Math.min(explorerheight,explorerwidth);
        int x1 = (explorerreigonwidth-borderpadding-explorerdimension)/2;
        int y1 = (explorerreigonheight-borderpadding-explorerdimension)/2;

        SetBlockInterface.setBlock testFunc = (a) -> selectedBlock = a;
        this.explorerWidget = new ExplorerWidgetCallback(x1,y1,explorerdimension,explorerdimension,Component.nullToEmpty("PaletteExplorer"),paletteData,testFunc);
        addRenderableWidget(explorerWidget);

        //Positoning frames Values,
        int framepadding = 10;
        //Selected Block Frame
        selectedblockframeX = width/2+framepadding;
        selectedblockframeY = framepadding;
        selectedblockframeW = width/2-2*framepadding;
        selectedblockframeH = 50;

        //Search Frame
        searchFrameX = selectedblockframeX;
        searchFrameY = selectedblockframeY + selectedblockframeH + framepadding;
        searchFrameW = selectedblockframeW;
        searchFrameH = height - framepadding - searchFrameY;

        //Pallet Frame

        paletteFrameX = borderpadding;
        paletteFrameY = height/2 + borderpadding;
        paletteFrameW = width/2 - 2*borderpadding;
        paletteFrameH = height/2 - 2*borderpadding;




        //'Standard' Widget Sizes
        int standardBlockinfoW = 70;
        int standardBlockinfoH = 25;
        //Block info widget in top right displaying currently selected block

        int blockinfox = selectedblockframeX+borderpadding;
        int blockinfoy = selectedblockframeY+borderpadding+textsize;
        int blockinfoh = standardBlockinfoH;
        int blockinfow = standardBlockinfoW;

        this.selectedBlockInfoWidget = new BlockInfoWidget(blockinfox, blockinfoy, blockinfow, blockinfoh, Component.literal("blockinfo"), paletteData);
        addRenderableWidget(selectedBlockInfoWidget);


        //Button that adds selected block to palettelist
        int addButtonW = 80;
        int addButtonH = 20;
        int addButtonX = blockinfox + blockinfow + borderpadding;
        int addButtonY = blockinfoy + borderpadding;
        addRenderableWidget(new Button(addButtonX,addButtonY,addButtonW,addButtonH,Component.literal("Add to Palette"),btn -> {
            testBlockList.add(this.selectedBlock);
            testScrollWidget.setN(testScrollWidget.getN()+1); //increment N as you increased size of list
        }));




        int paletteListSlots = 3;
        this.testScrollWidget = new ScrollWidgetList(paletteFrameX +borderpadding*2, paletteFrameY +borderpadding*2+textsize, paletteFrameW -borderpadding*4, paletteFrameH -borderpadding*4-textsize-addButtonH,paletteListSlots,3);
        testScrollWidget.addWidgets();
        int paletteListN = Math.max(1,testBlockList.size()-paletteListSlots);
        testScrollWidget.setN(paletteListN);

        int buttonspacing = testScrollWidget.innerheight /3;
        addRenderableWidget(new Button(paletteFrameX+borderpadding, paletteFrameY + paletteFrameH -addButtonH - borderpadding,addButtonW,addButtonH,Component.literal("Remove"), btn -> {
            if(testBlockList.size() > testScrollWidget.selectedslot) {
                testBlockList.remove(testScrollWidget.getScrollN()+testScrollWidget.selectedslot);
                if(testScrollWidget.getN() > 0){
                    testScrollWidget.setN(testScrollWidget.getN()-1);
                    testScrollWidget.scrollSubWidget.scrolltoN(testScrollWidget.getScrollN()-1);
                }
            }
            //testScrollWidget.setN(testScrollWidget.getN() -1); //decrement N as you decreased size of list
        }));


        this.searchWidget = new SearchWidget(blockinfox,searchFrameY+textsize,searchFrameW-borderpadding*4,searchFrameH,3);
        searchWidget.addWidgets();

        super.init();
    }

    @Override
    public void tick() {
        //removeWidget(this.explorerWidget);
        //LOGGER.info(String.valueOf(testScrollWidget.getN()));
        this.selectedBlockInfoWidget.setBlock(this.selectedBlock);
        this.searchResult = findinverse(selectedBlock);
        //this.testInvertedBlockInfoWidget.setBlock(this.searchResult);


        //TESTSCROLLWIDGET TICK TODO should go inside class

        int scrolloffset = testScrollWidget.getScrollN();

        int paletteListN = Math.max(0,testBlockList.size()-testScrollWidget.numslots);
        testScrollWidget.setN(paletteListN);
        testScrollWidget.tick();

        for(int i = 0; i< testScrollWidget.getNumslots(); i++){

            int listindex = i+scrolloffset;
            if(testBlockList.size() > listindex){

                testScrollWidget.setblock(i,testBlockList.get(i+scrolloffset));
            }
            else{

                testScrollWidget.setblock(i,Blocks.AIR);
            }


        }
        //SEARCHWIDGET TICK TODO should go inside class

        int scrolloffsetsearch = searchWidget.getScrollN();

        int paletteListNsearch = Math.max(0,searchWidget.searchresults.size()-searchWidget.results.numslots);
        searchWidget.results.setN(paletteListNsearch);
        searchWidget.results.tick();

        for(int i = 0; i< searchWidget.getNumslots(); i++){

            int listindex = i+scrolloffsetsearch;
            if(searchWidget.searchresults.size() > listindex){

                searchWidget.results.setblock(i,searchWidget.searchresults.get(i+scrolloffsetsearch).getFirst());
            }
            else{

                searchWidget.results.setblock(i,Blocks.AIR);
            }


        }



        //********************************************



        super.tick();
    }


    private Block getInverseBlock() {
        return this.searchResult;
    }

    private Block getSelectedBlock() {
        return this.selectedBlock;
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        explorerWidget.keyPressed(pKeyCode,pScanCode,pModifiers);
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private Block findinverse(Block selectedBlock) {
        int[] rgba = paletteData.getBlockRGB(selectedBlock);
        int invr = 255- rgba[0];
        int invg = 255- rgba[1];
        int invb = 255- rgba[2];
        int[] invrgb = new int[] {invr,invg,invb};
        //LOGGER.info(String.valueOf(invrgb));
        return paletteData.findNearest(invrgb);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        //explorerWidget.onClick(pMouseX,pMouseY);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onClose() {
        List<ItemStack> items = new ArrayList<>();
        for(Block block: testBlockList){

            items.add(Item.byBlock(block).getDefaultInstance());
        }
        Messages.sendToServer(new TextureWandPalletClosePacket(items));
        super.onClose();
    }

    /*
    SUBWIDGETS BELOW THIS POINT. Would put them in seperate class files but they need direct access to private variables in the screen instance and I am bad at java
     */

    class SearchWidget{
        /*
        Hardcoded gradient mode for now
         */
        int fullx,fully,fullwidth,fullheight,border;

        BlockInfoWidget  gradStart,gradEnd;
        Button searchButton,gradStartSelect,gradEndSelect,addPaletteButton;


        ScrollWidgetList results;
        List<Pair<Block,int[]>> searchresults;


        SearchWidget(int x, int y, int width, int height,int border){
            this.fullx = x;
            this.fully = y;
            this.fullwidth = width;
            this.fullheight = height;
            this.border = border;
            this.searchresults = new ArrayList<>();
        }

        void addWidgets(){
            int blockinfoW = (fullwidth - 10)/2;//100;
            int blockinfoH = 25;

            int buttonW = 50;
            int buttonH = 15;

            gradStart = new BlockInfoWidget(fullx+border,fully+border,blockinfoW,blockinfoH,Component.literal("gradStart"),paletteData);

            gradStartSelect = new Button(fullx+border,fully+border+blockinfoH,buttonW,buttonH,Component.literal("Set Start"),btn -> {
                gradStart.setBlock(selectedBlock);
            });

            gradEnd = new BlockInfoWidget(fullx+border*2+blockinfoW,fully+border,blockinfoW,blockinfoH,Component.literal("gradEnd"),paletteData);

            gradEndSelect = new Button(fullx+border*2+blockinfoW,fully+border+blockinfoH,buttonW,buttonH,Component.literal("Set End"),btn -> {
                gradEnd.setBlock(selectedBlock);
            });

            searchButton = new Button(fullx+border+fullwidth/2-buttonW/2,fully+border*2+blockinfoH+buttonH,buttonW,buttonH,Component.literal("Search"),btn -> {

                this.searchresults = paletteData.getGradient(gradStart.getBlock(),gradEnd.getBlock(),30);
            });

            int resultW = fullwidth;
            int resultH = fullheight-3*buttonH-blockinfoH-border*4-textsize;//120;
            int numslots =3;
            int buffer = 3;

            results = new ScrollWidgetList(fullx+border,fully+border*4+blockinfoH+buttonH*2,resultW,resultH,numslots,buffer);

            addPaletteButton = new Button(fullx+border,fully+border*5+blockinfoH+buttonH*2+resultH,buttonW*3,buttonH,Component.literal("Add Selected Block to Pallet"),btn ->{
                int index =  this.results.scrollSubWidget.scrollslot + this.results.selectedslot;
                if (index >= 0 && index < this.searchresults.size()){
                Block addblock = this.searchresults.get(this.results.scrollSubWidget.scrollslot+this.results.selectedslot).getFirst();
                LOGGER.info(addblock.toString());
                if (addblock != Blocks.AIR ) {
                    testBlockList.add(addblock);
                    testScrollWidget.setN(testScrollWidget.getN() + 1); //increment N as you increased size of list
                }
            }});

            addRenderableWidget(gradStart);
            addRenderableWidget(gradEnd);
            addRenderableWidget(gradStartSelect);
            addRenderableWidget(gradEndSelect);
            addRenderableWidget(searchButton);
            results.addWidgets();
            addRenderableWidget(addPaletteButton);

        }




        public int getScrollN() {
            return this.results.getScrollN();
        }

        public int getNumslots() {
            return results.getNumslots();
        }
    }


    class ScrollWidgetList{

        int totalx;
        int totaly;
        int totalwidth;
        int totalheight;

        int numslots;
        int slotborderbuffer;
        int innerheight;

        int scrollwidth = 10;

        //selection
        int selectedslot = 0;

        QuantizedScrollSubWidget scrollSubWidget;
        List<BlockInfoWidget> blockWidgetSlots;

        ScrollWidgetList(int x, int y, int width, int height, int numslots,int slotborderbuffer){
            this.totalx = x;
            this.totaly = y;
            this.totalwidth = width;
            this.totalheight = height;
            this.numslots = numslots;
            this.slotborderbuffer = slotborderbuffer;
            this.innerheight = (height - slotborderbuffer*2)/3;
            this.blockWidgetSlots = new ArrayList<>();
        }

        void addWidgets(){
            int slotwidth = totalwidth - scrollwidth - slotborderbuffer;
            for(int slot=0; slot < numslots; slot++){

                int slotx = totalx;
                int sloty = totaly+(innerheight+slotborderbuffer)*slot;

                int finalSlot = slot;
                blockWidgetSlots.add(new BlockInfoWidget(slotx,sloty,slotwidth,innerheight,Component.literal("BlockInfo"),paletteData, test -> {
                    this.selectedslot = finalSlot;
                    return 0;
                }));
                addRenderableWidget(blockWidgetSlots.get(blockWidgetSlots.size()-1));
            }
            this.scrollSubWidget = new QuantizedScrollSubWidget(totalx+totalwidth-scrollwidth,totaly,scrollwidth,totalheight,Component.literal("scrollbar"),numslots);
            addRenderableWidget(scrollSubWidget);
        }

        void tick(){
            for(int slot=0; slot < numslots; slot++){
                if(this.selectedslot == slot){
                    blockWidgetSlots.get(slot).focused = true;
                }
                else {
                    blockWidgetSlots.get(slot).focused = false;
                }
            }
        }


        int getN(){
            return this.scrollSubWidget.getN();
        }
        void setN(int n) {this.scrollSubWidget.setN(n);}

        int getNumslots(){
            return numslots;
        }

        void setblock(int n, Block block){
            blockWidgetSlots.get(n).setBlock(block);
        }

        public int getScrollN() {
            return scrollSubWidget.scrollslot;
        }
    }



}
