package com.woodenscalpel.buildinggizmos.misc.enumnbt;

import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.ShapeModes;
import net.minecraft.nbt.CompoundTag;

public class enumNbt {
    public static void setshapeenum(ShapeModes s,CompoundTag nbt, String tag){
        int i = s.ordinal();
        nbt.putInt(tag,i);
    }
    public static ShapeModes getshapeenum(CompoundTag nbt, String tag){
        int i = nbt.getInt(tag);
        return ShapeModes.values()[i];
    }
}
