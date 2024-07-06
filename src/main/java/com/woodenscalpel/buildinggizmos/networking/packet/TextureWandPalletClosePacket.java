package com.woodenscalpel.buildinggizmos.networking.packet;

import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;


public class TextureWandPalletClosePacket {
    public final List<ItemStack> itemstacks;
    public TextureWandPalletClosePacket(List<ItemStack> test){
        this.itemstacks = test;

    }

    public static TextureWandPalletClosePacket decode(FriendlyByteBuf buf){

       // List<ItemStack> items = buf.readList(FriendlyByteBuf::readItem);

        return new TextureWandPalletClosePacket(buf.readList(FriendlyByteBuf::readItem));
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeCollection(itemstacks,FriendlyByteBuf::writeItem);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //Serverside ??
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            ItemStack item = player.getMainHandItem();

            ((AbstractWand) item.getItem()).savePalette(itemstacks,item);


            //((TextureWand) item.getItem()).switchMode();
        });
        return true;

    }
}
