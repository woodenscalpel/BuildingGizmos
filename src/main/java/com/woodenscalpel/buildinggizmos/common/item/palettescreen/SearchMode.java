package com.woodenscalpel.buildinggizmos.common.item.palettescreen;

import net.minecraft.network.chat.Component;

public enum SearchMode {
    TEST1 (0, "test1"),
    TEST2 (1, "test2");

    private final int id;
    private final String key;

    private SearchMode(int id, String key){

            this.id = id;
            this.key = key;
        }

        public int getId() {
            return this.id;
        }

        public Component getDisplayName() {
            return Component.literal("MODE: " + this.key);
        }


    }

