package com.woodenscalpel.buildinggizmos.misc.enumnbt;

import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.ModeEnums;
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

    public static void setplacementenum(ModeEnums.PlacementModes s, CompoundTag nbt, String tag){
        int i = s.ordinal();
        nbt.putInt(tag,i);
    }
    public static ModeEnums.PlacementModes getplacementenum(CompoundTag nbt, String tag){
        int i = nbt.getInt(tag);
        return ModeEnums.PlacementModes.values()[i];
    }

    public static void setswapenum(ModeEnums.SwapModes s, CompoundTag nbt, String tag){
        int i = s.ordinal();
        nbt.putInt(tag,i);
    }
    public static ModeEnums.SwapModes getswapenum(CompoundTag nbt, String tag){
        int i = nbt.getInt(tag);
        return ModeEnums.SwapModes.values()[i];
    }
    public static void setpalettesourceenum(ModeEnums.PaletteSourceModes s, CompoundTag nbt, String tag){
        int i = s.ordinal();
        nbt.putInt(tag,i);
    }
    public static ModeEnums.PaletteSourceModes getpalettesourceenum(CompoundTag nbt, String tag){
        int i = nbt.getInt(tag);
        return ModeEnums.PaletteSourceModes.values()[i];
    }

}
