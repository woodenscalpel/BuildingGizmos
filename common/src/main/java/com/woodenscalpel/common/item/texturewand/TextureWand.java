package com.woodenscalpel.common.item.texturewand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.common.item.abstractwand.ModeEnums;
import com.woodenscalpel.misc.shapes.Box;
import com.woodenscalpel.networking.PacketRegister;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
        ShapeHelper.setShape(nbt, ShapeModes.FILLEDCUBE);
        super.inventoryTick(itemStack, level, entity, i, b);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {

        if (!context.getLevel().isClientSide() && context.getHand() == InteractionHand.MAIN_HAND) {
            CompoundTag nbt = context.getItemInHand().getOrCreateTag();
            if ((nbt.getInt(AbstractWand.activeStateTag) != AbstractWand.IN_USE)) {
                List<Vec3> cplist = AbstractWand.ShapeHelper.getControlPoints(nbt);
                if(cplist.size()>1) {
                    Box box = new Box(cplist.get(0), cplist.get(1));
                    if (box.contains(context.getClickedPos())) {
                        ((AbstractWand) context.getItemInHand().getItem()).build(context.getItemInHand());
                        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                        NetworkManager.sendToServer(PacketRegister.BUILD_PACKET_ID, buf);
                    }
                }


            }
        }
        return super.useOn(context);
    }
}

