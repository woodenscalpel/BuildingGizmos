package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class AbstractShape implements ShapeInterface{

    List<BlockPos> coords =new ArrayList<>();

    @Override
    public List<BlockPos> getCoords() {
        return coords;
    }
}
