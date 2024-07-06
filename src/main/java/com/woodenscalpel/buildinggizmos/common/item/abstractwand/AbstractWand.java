package com.woodenscalpel.buildinggizmos.common.item.abstractwand;

import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class AbstractWand extends Item implements PaletteInterface{

    //State machine states
    static final int SELECTING_P1 = 0;
    static final int SELECTING_P2 = 1;
    static final int IN_USE = 2;

    //Palette modes
    static final int MODE_HOTBAR = 0;
    static final int MODE_PALLET = 1;


    public AbstractWand(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    public void openPalletScreen(){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        //LOGGER.info("USED ON");
        if(!context.getLevel().isClientSide() && context.getHand() == InteractionHand.MAIN_HAND) {

            ItemStack item = context.getItemInHand();
            CompoundTag nbt = item.getOrCreateTag();



            if (!Objects.requireNonNull(context.getPlayer()).isCrouching()) {
                if (nbt.getBoolean("ready")) {


                    //if(context.getItemInHand().getOrCreateTag().getInt("ready") == 1){

                    BlockPos b1 = helpers.intarray2blockpos(nbt.getIntArray("P1"));
                    BlockPos b2 = helpers.intarray2blockpos(nbt.getIntArray("P2"));

                    List<BlockPos> controlPoints = new ArrayList<>();
                    controlPoints.add(b1);
                    controlPoints.add(b2);

                    Box area = new Box(b1,b2);

                    BlockPos blockClicked = context.getClickedPos();
                    if (area.contains(context.getClickedPos())) {

                        setBlockQueue(controlPoints,nbt);
                        nbt.putInt("state", IN_USE);
                    }
                    else{
                        //LOGGER.info("Not in area");
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

                        //LOGGER.info("set P1");


                        break;
                    case SELECTING_P2:
                        BlockPos p2 = context.getClickedPos();
                        nbt.putIntArray("P2", helpers.BlockPostoIntArray(p2));
                        nbt.putInt("state", SELECTING_P1);
                        nbt.putBoolean("ready", true);

                        //LOGGER.info("set P2");

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value:");
                }

            }
        }
        return super.useOn(context);
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        if (!level.isClientSide()) {
            CompoundTag nbt = itemStack.getOrCreateTag();

            if (nbt.getInt("state") == IN_USE) {

                //LOGGER.info("USING!");
                int[] rawBlockQueue = nbt.getIntArray("blockQueue");

                if(rawBlockQueue.length < 3) {

                    nbt.putInt("state", SELECTING_P1);
                }

            if (nbt.getInt("state") == IN_USE) {

                BlockPos nextblock = new BlockPos(rawBlockQueue[0],rawBlockQueue[1],rawBlockQueue[2]);
                nbt.putIntArray("blockQueue",helpers.arraySlice(rawBlockQueue,3,rawBlockQueue.length));

                if(entity instanceof Player player) {
                    processCoord(player,level,itemStack,nextblock);
                }



            }}
            super.inventoryTick(itemStack, level, entity, i, b);
        }
    }


    public void switchMode(Player player){
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        int CURRENT_MODE = nbt.getInt("MODE");
        switch (CURRENT_MODE){
            case MODE_HOTBAR:

                //LOGGER.info("switching mode to 1");
                player.sendSystemMessage(Component.literal("Pressed Mode Switch"));

                nbt.putInt("MODE",MODE_PALLET);
                //LOGGER.info(String.valueOf(nbt.getInt("MODE")));
                break;
            case MODE_PALLET:
                nbt.putInt("MODE",MODE_HOTBAR);
                break;
        }
    }

    public List<ItemStack> getPaletteFromMode(Player player,ItemStack wand, int mode) {
        switch (mode) {
            case MODE_HOTBAR -> {
                return getHotbarpalette(player);
            }
            case MODE_PALLET -> {
                return getPaletteItems(wand);
            }
        }
        return null;
    }

    private List<ItemStack> getHotbarpalette(Player player) {
        List<ItemStack> blocks = new ArrayList<>();
        for(int i = 0; i<=8;i++) {
            ItemStack itemi =  player.getInventory().getItem(i);
            if(itemi.getItem() instanceof BlockItem){
                blocks.add(itemi);
            }
        }
        return  blocks;
    }

    private ItemStack RandomItemStack(List<ItemStack> list){

        if(list.isEmpty()){
            return null;
        }
        int rand = RandomSource.createNewThreadLocalInstance().nextInt(list.size());
        return list.get(rand);
    }

    protected ItemStack getRandomFromPallet(Player player, ItemStack itemStack){
        CompoundTag nbt = itemStack.getOrCreateTag();
        int MODE = nbt.getInt("MODE");
        //LOGGER.info(String.valueOf(MODE));
        //LOGGER.info(Arrays.toString(nbt.getIntArray("palletIDs")));

        List<ItemStack> palette = getPaletteFromMode(player,itemStack,MODE);
        return RandomItemStack(palette);
    }

    protected abstract void processCoord(Player player, Level level, ItemStack itemStack, BlockPos nextblock);

    protected abstract void setBlockQueue(List<BlockPos> controlPoints, CompoundTag nbt);
}

