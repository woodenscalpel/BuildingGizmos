package com.woodenscalpel.buildinggizmos.misc.InteractionLayer;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.slf4j.Logger;

public class WorldInventoryInterface {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void swapBlock(Player player, ItemStack item, Level level, BlockPos pos){
        if(canDestroy(level,pos) && validSwap(level,pos) &&(hasItem(player,item) || player.isCreative())){
            destroyBlock(player,level,pos,true);
            if(!player.isCreative()) {
                removeItem(player, item);
            }
            placeBlock(player, item, level, pos);
        }

    }

    public static void safePlaceBlock(Player player, ItemStack item, Level level, BlockPos pos){
        if(canPlace(level,pos) &&(hasItem(player,item) || player.isCreative())){
            if(!player.isCreative()) {
                removeItem(player, item);
            }
            placeBlock(player, item, level, pos);
        }


    }

    public static boolean validSwap(Level level, BlockPos pos){
        return level.getBlockState(pos) != Blocks.AIR.defaultBlockState();
    }

    public static boolean canDestroy(Level level, BlockPos pos){
        return level.getBlockState(pos) != Blocks.BEDROCK.defaultBlockState() && !level.getBlockState(pos).hasBlockEntity();
    }
    public static boolean canPlace(Level level, BlockPos pos){
        return level.getBlockState(pos) == Blocks.AIR.defaultBlockState();
    }

    public static void placeBlock(Player player, ItemStack item, Level level, BlockPos position){
        if (canPlace(level,position)){
            level.setBlockAndUpdate(position, Block.byItem(item.getItem()).defaultBlockState());
        }else{
            LOGGER.info("Couldn't place");
        }
    }

    public static void  destroyBlock(Player player, Level level, BlockPos position,boolean dropblock) {
        level.destroyBlock(position,dropblock);
    }

    public static boolean canPlaceAt(Level level, BlockPos nextblock) {
        return true;
        //return level.getBlockState(nextblock) == Blocks.AIR.defaultBlockState();
    }

    public static boolean hasItem(Player player, ItemStack item) {

        return player.getInventory().contains(item);
    }

    public static void removeItem(Player player, ItemStack item) {
        int slot = player.getInventory().findSlotMatchingItem(item);
        player.getInventory().getItem(slot).shrink(1);
    }
}
