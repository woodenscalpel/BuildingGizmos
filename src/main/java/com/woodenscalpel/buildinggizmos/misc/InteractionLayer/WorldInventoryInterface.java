package com.woodenscalpel.buildinggizmos.misc.InteractionLayer;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

public class WorldInventoryInterface {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void placeBlock(Player player, ItemStack item, Level level, BlockPos position){
        if (level.getBlockState(position) == Blocks.AIR.defaultBlockState()){
            LOGGER.info("IS AIR");


            level.setBlockAndUpdate(position, Block.byItem(item.getItem()).defaultBlockState());
        }else{
            LOGGER.info("IS NOT AIR");
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
