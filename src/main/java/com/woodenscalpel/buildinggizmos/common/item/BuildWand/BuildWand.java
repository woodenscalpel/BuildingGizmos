package com.woodenscalpel.buildinggizmos.common.item.BuildWand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.*;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.ControlPoint;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCircle;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCubicBezier;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedQuadBezier;
import com.woodenscalpel.buildinggizmos.misc.Raycast;
import com.woodenscalpel.buildinggizmos.misc.enumnbt.enumNbt;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;



public class BuildWand extends AbstractWand {
    private static final Logger LOGGER = LogUtils.getLogger();


    public BuildWand(Properties properties) {
        super(properties.stacksTo(1));
    }



    @Override
    protected void processCoord(Player player, Level level, ItemStack wand, BlockPos nextblock) {
        //ItemStack item = getRandomFromPallet(player,wand);

        int queuelen = wand.getOrCreateTag().getInt("queueLen");
        CompoundTag nbt = wand.getOrCreateTag();
        int blocklen = (ShapeHelper.getQueue(nbt).size()+1);

        BuildingGizmos.LOGGER.info("LOG");
        BuildingGizmos.LOGGER.info(String.valueOf(queuelen));
        BuildingGizmos.LOGGER.info(String.valueOf(blocklen));

        float percentage = (float) blocklen/queuelen;

        BuildingGizmos.LOGGER.info(String.valueOf(percentage));

        ItemStack item = getGradientFromPallet(player,wand,percentage);

        //WorldInventoryInterface.placeBlock(player, Blocks.STONE.asItem().getDefaultInstance(),level,nextblock);
        WorldInventoryInterface.safePlaceBlock(player, item ,level,nextblock);

    }


    public void switchBuildMode(Player player) {
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        AbstractWand.ShapeHelper.cycleShape(nbt);
    }
}

