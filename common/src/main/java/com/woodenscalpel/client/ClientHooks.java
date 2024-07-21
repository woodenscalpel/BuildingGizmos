package com.woodenscalpel.client;

import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.common.item.texturewand.TextureWandPaletteScreen;
import net.minecraft.client.Minecraft;

public class ClientHooks {
    public static void openGradientScreen(){
        Minecraft.getInstance().setScreen(new TextureWandPaletteScreen(net.minecraft.network.chat.Component.empty()));

    }
}
