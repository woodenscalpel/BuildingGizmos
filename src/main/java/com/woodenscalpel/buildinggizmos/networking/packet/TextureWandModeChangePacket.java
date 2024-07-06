package com.woodenscalpel.buildinggizmos.networking.packet;

import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;



public class TextureWandModeChangePacket {
    public TextureWandModeChangePacket(){

    }

    public TextureWandModeChangePacket(FriendlyByteBuf buf){

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
            ((AbstractWand) item.getItem()).switchMode(player);
        });
        return true;

    }
}
