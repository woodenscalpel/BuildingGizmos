package com.woodenscalpel.buildinggizmos.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_BUILDINGGIZMOS = "key.category.buildinggizmos.buildinggizmos";

    public static final String KEY_MODE_SWITCH = "key.buildinggizmos.mode_switch";
    public static final String KEY_PALLET_MENU = "key.buildinggizmos.pallet_menu";

    public static final KeyMapping MODE_SWITCH_KEY = new KeyMapping(KEY_MODE_SWITCH, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, KEY_CATEGORY_BUILDINGGIZMOS);
    public static final KeyMapping PALLET_MENU_KEY = new KeyMapping(KEY_PALLET_MENU, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_BUILDINGGIZMOS);
}
