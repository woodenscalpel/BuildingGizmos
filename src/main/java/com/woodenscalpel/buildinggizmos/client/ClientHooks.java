package com.woodenscalpel.buildinggizmos.client;

import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWandPaletteScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ClientHooks {
    public static void openTextureWandScreen(){
        Minecraft.getInstance().setScreen(new TextureWandPaletteScreen(net.minecraft.network.chat.Component.empty()));
    };

}

