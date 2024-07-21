package com.woodenscalpel.common.item.texturewand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.common.item.abstractwand.ModeEnums;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


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
        ShapeHelper.setShape(nbt, ShapeModes.FILLEDCUBE);
        super.inventoryTick(itemStack, level, entity, i, b);
    }
}

