package com.woodenscalpel.buildinggizmos.networking.packet;

import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class WandBuildPacket {
    public WandBuildPacket(){

    }

    public WandBuildPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //Serverside ??
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            ItemStack item = player.getMainHandItem();

            ((AbstractWand) item.getItem()).build(item);
        });
        return true;

    }
}
