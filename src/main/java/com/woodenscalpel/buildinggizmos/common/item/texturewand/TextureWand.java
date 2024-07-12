package com.woodenscalpel.buildinggizmos.common.item.texturewand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.client.keys.KeyBinding;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.ModeEnums;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class TextureWand  extends AbstractWand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TextureWand(Properties properties) {
        super(properties.stacksTo(1));
    }


    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, Level level, @NotNull Entity entity, int i, boolean b) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        //TODO set this on creation instead of every tick
        AbstractWand.setSwapMode(nbt, ModeEnums.SwapModes.SWAP);
        AbstractWand.ShapeHelper.setShape(nbt, ShapeModes.FILLEDCUBE);
        super.inventoryTick(itemStack, level, entity, i, b);
    }
}

