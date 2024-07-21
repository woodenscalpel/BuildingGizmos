package com.woodenscalpel.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;


public class KeyBinding {
    public static final KeyMapping MODESWITCH_KEYMAPPING = new KeyMapping(
            "key.buildinggizmos.mode_switch", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_M, // The default keycode
            "key.category.buildinggizmos.buildinggizmos" // The category translation key used to categorize in the Controls screen
    );
    public static final KeyMapping SHAPESWITCH_KEYMAPPING = new KeyMapping(
            "key.buildinggizmos.shape_switch", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_B, // The default keycode
            "key.category.buildinggizmos.buildinggizmos" // The category translation key used to categorize in the Controls screen
    );
    public static final KeyMapping PALETTEMENU_KEYMAPPING = new KeyMapping(
            "key.buildinggizmos.pallet_menu", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_G, // The default keycode
            "key.category.buildinggizmos.buildinggizmos" // The category translation key used to categorize in the Controls screen
    );
    public static final KeyMapping BUILD_KEYMAPPING = new KeyMapping(
            "key.buildinggizmos.build", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_X, // The default keycode
            "key.category.buildinggizmos.buildinggizmos" // The category translation key used to categorize in the Controls screen
    );
    public static final KeyMapping PLACEMENTMODE_KEYMAPPING = new KeyMapping(
            "key.buildinggizmos.placementmode", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_C, // The default keycode
            "key.category.buildinggizmos.buildinggizmos" // The category translation key used to categorize in the Controls screen
    );
    public static final KeyMapping SWAPMODE_KEYMAPPING = new KeyMapping(
            "key.buildinggizmos.swapmode", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_Z,// The default keycode
            "key.category.buildinggizmos.buildinggizmos" // The category translation key used to categorize in the Controls screen
    );




    public static void register(){
        KeyMappingRegistry.register(MODESWITCH_KEYMAPPING);
        KeyMappingRegistry.register(SWAPMODE_KEYMAPPING);
        KeyMappingRegistry.register(SHAPESWITCH_KEYMAPPING);
        KeyMappingRegistry.register(BUILD_KEYMAPPING);
        KeyMappingRegistry.register(PLACEMENTMODE_KEYMAPPING);
        KeyMappingRegistry.register(PALETTEMENU_KEYMAPPING);
    }

}
