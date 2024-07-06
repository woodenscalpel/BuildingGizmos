package com.woodenscalpel.buildinggizmos.common.item.BuildWand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.Quantization.Bresenhm3D;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class BuildWand extends AbstractWand {
    private static final Logger LOGGER = LogUtils.getLogger();

    int TESTLINE = 0;
    int CURRENTSHAPE = 0;



    public BuildWand(Properties properties) {
        super(properties.stacksTo(1));
    }


    public void openPalletScreen(){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);
    }

    @Override
    protected void processCoord(Player player, Level level, ItemStack itemStack, BlockPos nextblock) {

        WorldInventoryInterface.placeBlock(player, Blocks.STONE.asItem().getDefaultInstance(),level,nextblock);

    }

    @Override
    protected void setBlockQueue(List<BlockPos> controlPoints, CompoundTag nbt) {
        BlockPos p1 = controlPoints.get(0);
        BlockPos p2 = controlPoints.get(1);


        List<BlockPos> linepos = new Bresenhm3D().drawLine(p1,p2);
        helpers.putBlockList(nbt,"blockQueue", (ArrayList<BlockPos>) linepos);
        LOGGER.info(String.valueOf(linepos));
    }
}

