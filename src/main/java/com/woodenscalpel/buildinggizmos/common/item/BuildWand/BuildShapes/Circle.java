package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

import com.woodenscalpel.buildinggizmos.misc.Quantization.Bresenham3D;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;

import java.util.List;

public class Circle extends AbstractShape{

    public Circle(BlockPos start, BlockPos end){
        double r = helpers.blockPostoVec3(end).subtract(helpers.blockPostoVec3(start)).length();
        List<Tuple> circ2d = new Bresenham3D().plot2DCircle(start.getX(),start.getZ(),(int) r);
        for(Tuple t : circ2d){
            coords.add(new BlockPos((int) t.getA(),start.getY(),(int)t.getB()));
        }
    }
}
