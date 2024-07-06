package com.woodenscalpel.buildinggizmos.common.item.abstractwand;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public interface PaletteInterface {

    public default void savePalette(List<ItemStack> palette, ItemStack wand) {
        CompoundTag nbt = wand.getOrCreateTag();
        List<Integer> itemids = new ArrayList<>();
        for(ItemStack item : palette){
            itemids.add(Item.getId(item.getItem()));
        }
        nbt.putIntArray("palletIDs",itemids);
    }

    public default List<ItemStack> getPaletteItems(ItemStack wand){
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

    public default List<Block> getPaletteBlocks(ItemStack wand){
        List<ItemStack> itemStacks = getPaletteItems(wand);
        List<Block> blocks = new ArrayList<>();
        for(ItemStack item : itemStacks){
            blocks.add(Block.byItem(item.getItem()));
        }
        return blocks;

    }
}
