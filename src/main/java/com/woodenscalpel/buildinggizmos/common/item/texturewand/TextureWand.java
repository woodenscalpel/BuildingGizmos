package com.woodenscalpel.buildinggizmos.common.item.texturewand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.slf4j.Logger;


public class TextureWand  extends AbstractWand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TextureWand(Properties properties) {
        super(properties.stacksTo(1));
    }


    public void openPalletScreen(){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);
    }

    @Override
    protected void processCoord(Player player, Level level, ItemStack itemStack, BlockPos nextblock) {
        ItemStack item = getRandomFromPallet(player,itemStack);

        if (item != null && WorldInventoryInterface.canPlaceAt(level, nextblock) && (WorldInventoryInterface.hasItem(player, item) || player.isCreative())) {

            WorldInventoryInterface.destroyBlock(player,level,nextblock,true);
            if(!player.isCreative()) {
                WorldInventoryInterface.removeItem(player, item);
            }
            WorldInventoryInterface.placeBlock(player, item, level, nextblock);
        }

    }

}

