package com.woodenscalpel.buildinggizmos.common.item.texturewand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.slf4j.Logger;

import java.util.List;


public class TextureWand  extends AbstractWand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TextureWand(Properties properties) {
        super(properties.stacksTo(1));
    }



    @Override
    protected void processCoord(Player player, Level level, ItemStack itemStack, BlockPos nextblock) {
        //Decide what block to place at the coord. Takes mode into account
        ItemStack item = getRandomFromPallet(player,itemStack);

        //Checks if operation is valid and handle removing item from inventory, creative mode, etc #TODO abstract this into WorldInventoryInterface
        if (item != null && WorldInventoryInterface.canPlaceAt(level, nextblock) && (WorldInventoryInterface.hasItem(player, item) || player.isCreative())) {

            WorldInventoryInterface.destroyBlock(player,level,nextblock,true);
            if(!player.isCreative()) {
                WorldInventoryInterface.removeItem(player, item);
            }
            WorldInventoryInterface.placeBlock(player, item, level, nextblock);
        }

    }

    @Override
    protected void setBlockQueue(List<BlockPos> controlPoints, CompoundTag nbt){

        Box area = new Box(controlPoints.get(0),controlPoints.get(1));
        helpers.putBlockList(nbt,"blockQueue",area.getBlockList());

    }

}

