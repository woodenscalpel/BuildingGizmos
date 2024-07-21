package com.woodenscalpel.common.item.abstractwand;

public class ModeEnums {
    public enum SwapModes{
        BUILD("modebuild"),SWAP("modeswap"),SWAP_AND_BUILD("modeswapandbuild");

        public final String name;
        SwapModes(String pname){
            name = "buildinggizmos."+pname;
        }

        public SwapModes cycle(){
            int length = values().length;
            int next = ordinal()+1;
            if (next >=length){
                next -= length;
            }
            return values()[next];
        }
    }
    public enum PlacementModes{
        RANDOM("moderandom"),GRADIENT("modegradient");

        public final String name;
        PlacementModes(String pname){
            name = "buildinggizmos."+pname;
        }

        public PlacementModes cycle(){
            int length = values().length;
            int next = ordinal()+1;
            if (next >=length){
                next -= length;
            }
            return values()[next];
        }
    }

    public enum PaletteSourceModes{
        HOTBAR("hotbar"),PALETTEMENU("palettemenu");

        public final String name;
        PaletteSourceModes(String pname) {
            name = "buildinggizmos."+pname;
        }

        public PaletteSourceModes cycle(){
            int length = values().length;
            int next = ordinal()+1;
            if (next >=length){
                next -= length;
            }
            return values()[next];
        }
    }
}
