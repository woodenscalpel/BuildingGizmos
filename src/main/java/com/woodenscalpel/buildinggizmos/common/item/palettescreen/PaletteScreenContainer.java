package com.woodenscalpel.buildinggizmos.common.item.palettescreen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.misc.SetBlockInterface;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PaletteScreenContainer extends AbstractContainerScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    int borderpadding = 5;

    PaletteData paletteData;
    ExplorerWidgetCallback explorerWidget;

    BlockInfoWidget2 selectedBlockInfoWidget;
    BlockInfoWidget2 testInvertedBlockInfoWidget;

    ScrollWidgetList testScrollWidget;
    //List<Block> testBlockList = new ArrayList<>(List.of(new Block[]{Blocks.DIRT, Blocks.FURNACE, Blocks.GRASS_BLOCK, Blocks.BIRCH_LOG, Blocks.STONE, Blocks.ACACIA_FENCE, Blocks.BLUE_CANDLE, Blocks.DIAMOND_BLOCK, Blocks.MAGENTA_STAINED_GLASS}));
    List<Block> testBlockList = new ArrayList<>();

    SearchWidget searchWidget;

    Block selectedBlock;
    Block searchResult;

    public PaletteScreenContainer(AbstractContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        //get block palette data (once)
        this.paletteData = new PaletteData();
        this.selectedBlock = Blocks.STONE;
        this.searchResult = Blocks.STONE;

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


        //Block info widget in top right displaying currently selected block

        int blockinfox = width/2+10;
        int blockinfoy = y1;
        int blockinfoh = 50;
        int blockinfow = 100;

        this.selectedBlockInfoWidget = new BlockInfoWidget2(blockinfox, blockinfoy, blockinfow, blockinfoh, Component.literal("blockinfo"), paletteData);
        addRenderableWidget(selectedBlockInfoWidget);

        //Button that adds selected block to palettelist
        int addButtonW = 50;
        int addButtonH = 20;
        int addButtonX = blockinfox + blockinfow - addButtonW;
        int addButtonY = blockinfoy + blockinfoh + borderpadding;
        addRenderableWidget(new Button(addButtonX,addButtonY,addButtonW,addButtonH,Component.literal("Add to Palette"),btn -> {
            testBlockList.add(this.selectedBlock);
            testScrollWidget.setN(testScrollWidget.getN()+1); //increment N as you increased size of list
        }));


        int blocksearchx = blockinfox;
        int blocksearchy = blockinfoy + blockinfoh + borderpadding*2;

       // addRenderableWidget(new BlockSearchWidget(blocksearchx,blocksearchy,100,100,Component.literal("blockSearch")));

        this.testInvertedBlockInfoWidget = new BlockInfoWidget2(blocksearchx, blocksearchy + 150, 100, 100, Component.literal("antiblock"), paletteData);
        //addRenderableWidget(testInvertedBlockInfoWidget);

        //addRenderableWidget(new ExtendedButton(blocksearchx,blocksearchy+50,100,20,Component.literal("testbutton"),btn -> {}));
        //addRenderableWidget(CycleButton.onOffBuilder().create(blocksearchx-50,blocksearchy+100,100,20,Component.literal("testcycle"),(b,b2) -> {}));
        //addRenderableWidget(CycleButton.builder(SearchMode::getDisplayName).withValues(SearchMode.values()).withInitialValue(SearchMode.TEST1).create(100,200,100,20,Component.literal("cyclebuttontest"),(thisbutton,value) ->{LOGGER.info(String.valueOf(value));}));

        int paletteListSlots = 3;
        this.testScrollWidget = new ScrollWidgetList(borderpadding,height/2+borderpadding,100,160,paletteListSlots,3,50);
        testScrollWidget.addWidgets();
        int paletteListN = Math.max(1,testBlockList.size()-paletteListSlots);
        testScrollWidget.setN(paletteListN);


        this.searchWidget = new SearchWidget(blockinfox,addButtonY+addButtonH+borderpadding,200,175,3);
        searchWidget.addWidgets();

        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        //removeWidget(this.explorerWidget);
        this.selectedBlockInfoWidget.setBlock(this.selectedBlock);
        this.searchResult = findinverse(selectedBlock);
        this.testInvertedBlockInfoWidget.setBlock(this.searchResult);


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



    /*
    SUBWIDGETS BELOW THIS POINT. Would put them in seperate class files but they need direct access to private variables in the screen instance and I am bad at java
     */

    class SearchWidget{
        /*
        Hardcoded gradient mode for now
         */
        int fullx,fully,fullwidth,fullheight,border;

        BlockInfoWidget2 gradStart,gradEnd;
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
            int blockinfoW = 100;
            int blockinfoH = 50;

            int buttonW = 50;
            int buttonH = 20;

            gradStart = new BlockInfoWidget2(fullx+border,fully+border,blockinfoW,blockinfoH,Component.literal("gradStart"),paletteData);

            gradStartSelect = new Button(fullx+border,fully+border+blockinfoH,buttonW,buttonH,Component.literal("Set Start"),btn -> {
                gradStart.setBlock(selectedBlock);
            });

            gradEnd = new BlockInfoWidget2(fullx+border*2+blockinfoW,fully+border,blockinfoW,blockinfoH,Component.literal("gradEnd"),paletteData);

            gradEndSelect = new Button(fullx+border*2+blockinfoW,fully+border+blockinfoH,buttonW,buttonH,Component.literal("Set End"),btn -> {
                gradEnd.setBlock(selectedBlock);
            });

            searchButton = new Button(fullx+border,fully+border*2+blockinfoH+buttonH,buttonW,buttonH,Component.literal("Search"),btn -> {

                this.searchresults = paletteData.getGradient(gradStart.getBlock(),gradEnd.getBlock(),30);
                /*
                for(Pair<Block, int[]> pair : searchresults){
                    LOGGER.info(String.valueOf(pair.getFirst()) + Arrays.toString(pair.getSecond()));
                }

                 */
            });

            int resultW = 100;
            int resultH = 160;
            int numslots =3;
            int buffer = 3;
            int innterH = 50;

            results = new ScrollWidgetList(fullx+border,fully+border*4+blockinfoH+buttonH*2,resultW,resultH,numslots,buffer,innterH);

            addPaletteButton = new Button(fullx+border,fully+border*5+blockinfoH+buttonH*2+resultH,buttonW,buttonH,Component.literal("Add"),btn ->{
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
        List<BlockInfoWidget2> blockWidgetSlots;

        ScrollWidgetList(int x, int y, int width, int height, int numslots,int slotborderbuffer, int innerheight){
            this.totalx = x;
            this.totaly = y;
            this.totalwidth = width;
            this.totalheight = height;
            this.numslots = numslots;
            this.slotborderbuffer = slotborderbuffer;
            this.innerheight = innerheight;
            this.blockWidgetSlots = new ArrayList<>();
        }

        void addWidgets(){
            int slotwidth = totalwidth - scrollwidth - slotborderbuffer;
            for(int slot=0; slot < numslots; slot++){

                int slotx = totalx;
                int sloty = totaly+(innerheight+slotborderbuffer)*slot;

                //addRenderableWidget(new Button(slotx,sloty,slotwidth,innerheight,Component.literal("REE"),btn -> {}));
                int finalSlot = slot;
                blockWidgetSlots.add(new BlockInfoWidget2(slotx,sloty,slotwidth,innerheight,Component.literal("BlockInfo"),paletteData, test -> {
                    //LOGGER.info("E");
                    this.selectedslot = finalSlot;
                    return 0;
                }));
                addRenderableWidget(blockWidgetSlots.get(blockWidgetSlots.size()-1));
                //addRenderableWidget(new BlockInfoWidget2(slotx,sloty,slotwidth,innerheight,Component.literal("BlockInfo"),paletteData));
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
