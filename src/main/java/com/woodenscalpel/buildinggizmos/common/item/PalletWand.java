package com.woodenscalpel.buildinggizmos.common.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.palettescreen.PaletteMenu;
import com.woodenscalpel.buildinggizmos.common.item.palettescreen.PaletteScreen;
import com.woodenscalpel.buildinggizmos.common.item.palettescreen.PaletteScreenContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import com.woodenscalpel.buildinggizmos.misc.getTextures;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkHooks;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;

import java.util.List;

public class PalletWand extends Item {

    private static final Logger LOGGER = LogUtils.getLogger();
    public PalletWand(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
       // List<Pair<ResourceLocation, int[]>> test = new getTextures().getTextures();
       // LOGGER.debug(test.toString());
       // Screen pscreen = new PaletteScreen(net.minecraft.network.chat.Component.empty());
        /*
        PaletteMenu menu = new PaletteMenu(null,0);
        Screen pscreen2 = new PaletteScreenContainer(menu,pPlayer.getInventory(), Component.literal("TIT:E"));
         */

        /*

        pPlayer.openItemGui(pPlayer.getItemInHand(pUsedHand),pUsedHand);
        if(pLevel.isClientSide){
            LocalPlayer localPlayer = (LocalPlayer) pPlayer;
                Minecraft.getInstance().setScreen(pscreen);
        }

         */
        //pPlayer.getItemInHand(pUsedHand).get
        /*
        NetworkHooks.openScreen(new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new PaletteMenu(containerId, playerInventory),
                Component.translatable("menu.title.examplemod.mymenu")
        ));
         */

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openPalletWandScreen);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        List<Pair<Block, int[]>> test = new getTextures().getTextures();
        for (Pair<Block, int[]> item : test){
            Block  block  = item.getFirst();
            int r = item.getSecond()[0];
            int g = item.getSecond()[1];
            int b = item.getSecond()[2];

            int newx = pContext.getClickedPos().getX() + r/10;
            int newy = pContext.getClickedPos().getY() + g/10;
            int newz = pContext.getClickedPos().getZ() + b/10;

            BlockPos target = new BlockPos(newx,newy,newz);

            pContext.getLevel().setBlockAndUpdate(target,block.defaultBlockState());
        }

        return super.useOn(pContext);
    }
}
