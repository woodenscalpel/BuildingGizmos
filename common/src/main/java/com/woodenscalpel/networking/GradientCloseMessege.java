package com.woodenscalpel.networking;

import com.woodenscalpel.BuildingGizmos;
import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GradientCloseMessege {
    public List<ItemStack> posList = new ArrayList<>();

    public GradientCloseMessege(FriendlyByteBuf buf) {
        posList = buf.readList(FriendlyByteBuf::readItem);

    }

    public GradientCloseMessege(List<ItemStack> poslist) {
        this.posList = poslist;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(posList, FriendlyByteBuf::writeItem);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(() -> {
            //Serverside ??
            Player player = context.getPlayer();

            ItemStack item = player.getMainHandItem();

            ((AbstractWand) item.getItem()).savePalette(posList,item);
        });
    }

}
