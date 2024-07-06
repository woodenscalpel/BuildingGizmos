package com.woodenscalpel.buildinggizmos.common.item.texturewand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class BuildWand extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    int state = 0;
    static final int SELECTING_P1 = 0;
    static final int SELECTING_P2 = 1;
    static final int IN_USE = 2;

    //nbt putboolean and getboolean not working?????
    //boolean ready = false;


    static final int MODE_HOTBAR = 0;
    static final int MODE_PALLET = 1;


    public BuildWand(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand p_41434_) {
        //new getTextures();
        /*
        Screen pscreen = new GradientScreenTextureWand(net.minecraft.network.chat.Component.empty());
        if(pLevel.isClientSide){
            LocalPlayer localPlayer = (LocalPlayer) pPlayer;
            Minecraft.getInstance().setScreen(pscreen);
        }
         */
        LOGGER.info(String.valueOf(pPlayer.isSecondaryUseActive()));
        return super.use(pLevel, pPlayer, p_41434_);
    }



    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        LOGGER.info("USED ON");
        if(!context.getLevel().isClientSide() && context.getHand() == InteractionHand.MAIN_HAND) {

            ItemStack item = context.getItemInHand();
            CompoundTag nbt = item.getOrCreateTag();



            if (!Objects.requireNonNull(context.getPlayer()).isCrouching()) {
                if (nbt.getBoolean("ready")) {


                    //if(context.getItemInHand().getOrCreateTag().getInt("ready") == 1){

                    BlockPos b1 = helpers.intarray2blockpos(nbt.getIntArray("P1"));
                    BlockPos b2 = helpers.intarray2blockpos(nbt.getIntArray("P2"));
                    Box area = new Box(b1,b2);

                    BlockPos blockClicked = context.getClickedPos();
                    if (area.contains(context.getClickedPos())) {


                        helpers.putBlockList(nbt,"blockQueue",area.getBlockList());
                        nbt.putInt("state", IN_USE);
                    }
                    else{
                        LOGGER.info("Not in area");
                    }

                }


            } else {
                switch (nbt.getInt("state")) {
                    case IN_USE:
                        break;
                    case SELECTING_P1:
                        BlockPos p1 = context.getClickedPos();
                        nbt.putIntArray("P1", helpers.BlockPostoIntArray(p1));
                        nbt.putInt("state", SELECTING_P2);
                        nbt.putBoolean("ready", false);

                        LOGGER.info("set P1");


                        break;
                    case SELECTING_P2:
                        BlockPos p2 = context.getClickedPos();
                        nbt.putIntArray("P2", helpers.BlockPostoIntArray(p2));
                        nbt.putInt("state", SELECTING_P1);
                        nbt.putBoolean("ready", true);

                        LOGGER.info("set P2");

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value:");
                }

            }
        }
        return super.useOn(context);
    }

    public void openPalletScreen(){
        /*
        Player pPlayer = Minecraft.getInstance().player;
        Level pLevel = Minecraft.getInstance().level;
        Screen pscreen = new TextureWandPaletteScreen(net.minecraft.network.chat.Component.empty());
        InteractionHand pUsedHand = InteractionHand.MAIN_HAND; //TODO do this properly

        pPlayer.openItemGui(pPlayer.getItemInHand(pUsedHand),pUsedHand);
        if(pLevel.isClientSide){
            LocalPlayer localPlayer = (LocalPlayer) pPlayer;
            Minecraft.getInstance().setScreen(pscreen);
        }

         */
        //NetworkHooks.openScreen(pPlayer,);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);


    }
    public void setPallet(){
        //Called upon closing palletscreen, used to set blockpallet

    }


     public void switchMode(Player player){
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        int CURRENT_MODE = nbt.getInt("MODE");
        switch (CURRENT_MODE){
            case MODE_HOTBAR:

                LOGGER.info("switching mode to 1");
                player.sendSystemMessage(Component.literal("Pressed Mode Switch"));

                nbt.putInt("MODE",MODE_PALLET);
                LOGGER.info(String.valueOf(nbt.getInt("MODE")));
                break;
            case MODE_PALLET:
                nbt.putInt("MODE",MODE_HOTBAR);
                break;
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        if (!level.isClientSide()) {
            CompoundTag nbt = itemStack.getOrCreateTag();

            if (nbt.getInt("state") == IN_USE) {
                LOGGER.info("USING!");
                int[] rawBlockQueue = nbt.getIntArray("blockQueue");
                BlockPos nextblock = new BlockPos(rawBlockQueue[0],rawBlockQueue[1],rawBlockQueue[2]);
                nbt.putIntArray("blockQueue",helpers.arraySlice(rawBlockQueue,3,rawBlockQueue.length));

                if(entity instanceof Player player) {
                    ItemStack item = getRandomFromPallet(player,itemStack);
                    if (item != null && WorldInventoryInterface.canPlaceAt(level, nextblock) && (WorldInventoryInterface.hasItem(player, item) || player.isCreative())) {

                        //int slot = player.getInventory().findSlotMatchingItem(Item.BY_BLOCK.get(Blocks.STONE).getDefaultInstance());
                        //LOGGER.info(String.valueOf(slot));
                        //int slot = player.getInventory().getSlotWithRemainingSpace(Item.BY_BLOCK.get(Blocks.STONE).getDefaultInstance());
                        //LOGGER.info(String.valueOf(slot));
                        //level.setBlockAndUpdate(nextblock, Blocks.AIR.defaultBlockState());
                        WorldInventoryInterface.destroyBlock(player,level,nextblock,true);
                        if(!player.isCreative()) {
                            WorldInventoryInterface.removeItem(player, item);
                        }
                        WorldInventoryInterface.placeBlock(player, item, level, nextblock);
                    }
                }


                if(rawBlockQueue.length < 4) {

                    nbt.putInt("state", SELECTING_P1);
                }
            }
            super.inventoryTick(itemStack, level, entity, i, b);
        }
    }
    private ItemStack getRandomFromPallet(Player player, ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        int MODE = nbt.getInt("MODE");
        LOGGER.info(String.valueOf(MODE));
        LOGGER.info(Arrays.toString(nbt.getIntArray("palletIDs")));
        switch (MODE){
            case MODE_HOTBAR:
                return selectHotbarItem(player);
            case MODE_PALLET:
                return selectPalletItem(player, itemStack);
        }

    return null;
    }

    private ItemStack selectHotbarItem(Player player) {
        //return Item.BY_BLOCK.get(Blocks.STONE).getDefaultInstance();
        //ItemStack item =  player.getInventory().getItem(2);
        List<ItemStack> blocks = new ArrayList<>();
        for(int i = 0; i<=8;i++) {
            ItemStack itemi =  player.getInventory().getItem(i);
            if(itemi.getItem() instanceof BlockItem){
                blocks.add(itemi);
            }
        }
        if(blocks.isEmpty()){
            return null;
        }
        int rand = RandomSource.createNewThreadLocalInstance().nextInt(blocks.size());
        return blocks.get(rand);

    }

    private ItemStack selectPalletItem(Player player, ItemStack wand){
        CompoundTag nbt = wand.getOrCreateTag();
        int[] itemids;
        itemids = nbt.getIntArray("palletIDs");

        List<ItemStack> itemStacks = new ArrayList<>();
        for(int id : itemids){
            ItemStack istack = Item.byId(id).getDefaultInstance();
            if(istack.getItem() instanceof BlockItem){
                itemStacks.add(istack);
            }
        }

        if(itemStacks.isEmpty()){
            return null;
        }
        int rand = RandomSource.createNewThreadLocalInstance().nextInt(itemStacks.size());
        return itemStacks.get(rand);
    }

    public void savePalette(List<ItemStack> palette, ItemStack wand) {
        CompoundTag nbt = wand.getOrCreateTag();
       List<Integer> itemids = new ArrayList<>();
        for(ItemStack item : palette){
            itemids.add(Item.getId(item.getItem()));
        }
        nbt.putIntArray("palletIDs",itemids);
    }

    public List<ItemStack> getPaletteItems(ItemStack wand){
        CompoundTag nbt = wand.getOrCreateTag();
        int[] itemids;
        itemids = nbt.getIntArray("palletIDs");

        List<ItemStack> itemStacks = new ArrayList<>();
        for(int id : itemids){
            ItemStack istack = Item.byId(id).getDefaultInstance();
            if(istack.getItem() instanceof BlockItem){
                itemStacks.add(istack);
            }
        }
        return itemStacks;
    }

    public List<Block> getPaletteBlocks(ItemStack wand){
        List<ItemStack> itemStacks = getPaletteItems(wand);
        List<Block> blocks = new ArrayList<>();
        for(ItemStack item : itemStacks){
           blocks.add(Block.byItem(item.getItem()));
        }
        return blocks;

    }

    /*
    private List<Block> getGradient(){

    }
    private void setGradient(){

    }

     */

}

