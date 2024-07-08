package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

public enum ShapeModes {
    LINE,CIRCLE,CAT;

    public ShapeModes cycle(){
        int length = values().length;
        int current = ordinal()+1;
        if (current>=length){
            current -= length;
        }
        return values()[current];
    }
}

